package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.ChatModel
import com.padc.chatapplication.data.models.ChatModelImpl
import com.padc.chatapplication.data.models.ContactModel
import com.padc.chatapplication.data.models.ContactModelImpl
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.ChatPresenter
import com.padc.chatapplication.mvp.views.ChatView
import com.padc.chatapplication.utils.ConfigUtils

class ChatPresenterImpl:AbstractBasePresenter<ChatView>(),ChatPresenter {
    private val contactModel: ContactModel = ContactModelImpl
    private val chatModel: ChatModel = ChatModelImpl
    var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var chatList:MutableList<SmallChatVO> = mutableListOf()
    private var contactList:List<UserVO> = listOf()
    private var chatShortChatList:MutableList<SmallChatVO> = mutableListOf()
    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        //get contacts list
        contactModel.getAllContacts(currentUserId,{
            contactList=it
            mView.showContacts(it)
        },{
            mView.showError(it)
        })


        //get chat list
        chatModel.getAllChatListByUserId(currentUserId,{
            //mView.showShortChatList(it)
            updateShortChatList(it)
            chatModel.getAllGroupList(currentUserId,{
                var groups= it.sortedByDescending { groupVO -> groupVO.createdTimeStamp?.toLong() }
                //mView.saveGroupList(groups)
                changeGroupToSmallChatList(it)
            },{
                mView.showError(it)
            })

        },{
            mView.showError(it)
        })


    }

    private fun changeGroupToSmallChatList(groupChatList: List<GroupVO>) {

        chatList.clear()
       chatList= mutableListOf()
       chatList.addAll(chatShortChatList as MutableList<SmallChatVO>)
        groupChatList.forEach {
            var lastMsgKey:String?=it.messages?.keys?.max()
            var lstMsg=it.messages?.get(lastMsgKey)
            var smallChatVO =SmallChatVO(null,lstMsg?.message, lstMsg?.file,
                null,it,lstMsg?.sendTimeStamp,lstMsg?.senderName)

            chatList.add(smallChatVO)

        }
        mView.showShortChatList(chatList.sortedByDescending { chat->chat.lastSentTimeStamp })

    }


    override fun onTapChat(receipt:UserVO) {
      mView.showPeerToPeerChatConversation(
          receipt
      )
    }

    override fun onTapGroupChat(receipt: GroupVO) {
      mView.showGroupChat(receipt)
    }



   fun updateShortChatList(chats: List<SmallChatVO>) {

        chats.forEach {smallChatVO->
            var receipt :UserVO? = contactList.find { it.qrCode==smallChatVO.receiptUserId }
            receipt?.let {
                smallChatVO.receiptVO =it
            }
            if(smallChatVO.lstSentUserName== ConfigUtils.getInstance().getUserName()){
                smallChatVO.lstSentUserName="You"
            }
        }

       chatShortChatList.clear()
       chatShortChatList.addAll(chats as MutableList<SmallChatVO>)
       chatList= chats as MutableList<SmallChatVO>


    }
}