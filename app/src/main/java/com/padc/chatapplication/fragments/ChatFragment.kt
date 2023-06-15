package com.padc.chatapplication.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.padc.chatapplication.R
import com.padc.chatapplication.activities.GroupChatActivity
import com.padc.chatapplication.activities.PeerToPeerChatActivity
import com.padc.chatapplication.adapters.ChatPersonAdapter
import com.padc.chatapplication.adapters.SmallChatConversationAdapter
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.ChatPresenter
import com.padc.chatapplication.mvp.presenters.impls.ChatPresenterImpl
import com.padc.chatapplication.mvp.views.ChatView
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment(),ChatView {

lateinit var mChatPersonAdapter: ChatPersonAdapter
lateinit var mSmallConAdapter: SmallChatConversationAdapter
lateinit var mPresenter: ChatPresenter
private var contactList:List<UserVO> = listOf()
    private var groupChatList:List<GroupVO> = listOf()
    private var chatList:List<SmallChatVO> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setUpRecycler()
        context?.let { mPresenter.onUiReady(it,this) }
    }
    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[ChatPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }
    private fun setUpRecycler() {
       mChatPersonAdapter = ChatPersonAdapter(mPresenter)
        rvActivePerson.apply {
            adapter=mChatPersonAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    itemPosition: Int,
                    parent: RecyclerView
                ) {
                    super.getItemOffsets(outRect, itemPosition, parent)
                    outRect.left=45
                }
            })
        }


        //conversation
        mSmallConAdapter = SmallChatConversationAdapter(mPresenter)

        rvConverationChat.apply {
            adapter= mSmallConAdapter
            layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

            addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.top=10
                }
            })
        }

    }



    override fun showContacts(contacts: List<UserVO>) {
        contactList=contacts
      mChatPersonAdapter.setNewData(contacts)
    }

    override fun showShortChatList(chats: List<SmallChatVO>) {
        chatList=chats
        mSmallConAdapter.setNewData(chatList)
       // chatList = chats as MutableList
       /* chats.forEach {
            var pos:Int=chatList.indexOfFirst { s ->s.receiptUserId== it.receiptUserId}
            if(pos>=0){
                chatList.removeAt(pos)
            }
            chatList.add(it)
        }
        //chatList.addAll(chats)
        chatList.forEach {smallChatVO->
           var receipt :UserVO? = contactList.find { it.qrCode==smallChatVO.receiptUserId }
            receipt?.let {
                smallChatVO.receiptVO =it
              }
           if(smallChatVO.lstSentUserName== ConfigUtils.getInstance().getUserName()){
               smallChatVO.lstSentUserName="You"
           }
        }

        chatList.sortedByDescending { chat->chat.lastSentTimeStamp }
        mSmallConAdapter.setNewData(chatList)
        Log.i("chats", chatList.toString())*/

    }

    override fun showPeerToPeerChatConversation(receipt: UserVO) {
        var receiptJson=Gson().toJson(receipt)
        startActivity(context?.let { PeerToPeerChatActivity.newIntent(it,receiptJson) })
    }

    override fun saveGroupList(groupList: List<GroupVO>) {
        groupChatList = groupList

      /*  groupChatList.forEach {
            var lastMsgKey:String?=it.messages?.keys?.max()
            var lstMsg=it.messages?.get(lastMsgKey)
            var smallChatVO =SmallChatVO(null,lstMsg?.message, lstMsg?.file,
            null,it,lstMsg?.sendTimeStamp,lstMsg?.senderName)

            //remove same id
            var pos:Int=chatList.indexOfFirst { s ->s.groupVO?.groupName== it.groupName}
            if(pos>=0){
                chatList.removeAt(pos)
            }
            chatList.add(smallChatVO)

        }

        chatList.sortedByDescending { chat->chat.lastSentTimeStamp }
        mSmallConAdapter.setNewData(chatList)*/
    }

    override fun showGroupChat(groupVO: GroupVO) {
        var groupJson=Gson().toJson(groupVO)
        startActivity(context?.let { GroupChatActivity.newIntent(it,groupJson) })
    }

    override fun showError(error: String) {
       showError(error)
    }


}