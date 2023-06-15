package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.*
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.GroupChatPresenter
import com.padc.chatapplication.mvp.views.GroupChatView

class GroupChatPresenterImpl:AbstractBasePresenter<GroupChatView>(),GroupChatPresenter {
    private var currentUserVO: UserVO?=null
    private var groupChatModel:GroupChatModel=GroupChatModelImpl
    private val accountModel: AccountModel = AccountModelImpl
    private var chatModel:ChatModel = ChatModelImpl


    override fun sendGroupChatMessage(groupVO: GroupVO,msg: GroupMessageVO) {
        msg.senderId=currentUserVO?.qrCode
        msg.senderName=currentUserVO?.name
        msg.senderProfile=currentUserVO?.profileImage

        groupVO.lastMessage=msg.message
        groupVO.lastSendUserId=currentUserVO?.qrCode
        groupVO.lastMessageSentUserName=currentUserVO?.name
       groupChatModel.sendMessage(groupVO,msg)
    }

    override fun getGroupMsg(groupId: String) {
        //get groups list
        groupChatModel.getAllGroupMsg(groupId,{
            mView.showGroupChatMsg(it)
        },{
            mView.showError(it)
        })
    }

    override fun uploadImageAndSendGroupMsg(img: Bitmap, groupVO: GroupVO, msg: GroupMessageVO) {
        msg.senderId=currentUserVO?.qrCode
        msg.senderName=currentUserVO?.name
        msg.senderProfile=currentUserVO?.profileImage
        groupChatModel.uploadImageAndSendGroupMsg(img, groupVO, msg)
    }

    override fun uploadGifFileAndSendGroupMsg(file: Uri, groupVO: GroupVO, msg: GroupMessageVO) {
        msg.senderId=currentUserVO?.qrCode
        msg.senderName=currentUserVO?.name
        msg.senderProfile=currentUserVO?.profileImage
        groupChatModel.uploadGifAndSendGroupMsg(file, groupVO, msg)
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        accountModel.getUseInfoById(currentUserId,{
            currentUserVO = it

        },{
            mView.showError(it)
        })

    }
}