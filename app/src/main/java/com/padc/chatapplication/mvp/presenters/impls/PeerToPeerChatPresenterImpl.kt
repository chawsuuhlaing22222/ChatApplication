package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.AccountModel
import com.padc.chatapplication.data.models.AccountModelImpl
import com.padc.chatapplication.data.models.ChatModelImpl
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.PeerToPeerChatPresenter
import com.padc.chatapplication.mvp.views.PeerToPeerChatView

class PeerToPeerChatPresenterImpl:AbstractBasePresenter<PeerToPeerChatView>(),PeerToPeerChatPresenter {

    private var chatModel = ChatModelImpl
    private val accountModel: AccountModel = AccountModelImpl
    private var currentUserVO: UserVO?=null

    override fun onTapSendMessage(message: MessageVO) {
        message.lastMessageSentUserName=currentUserVO?.name
        chatModel.sendMessage(msg = message)
    }

    override fun onUiReady(
        context: Context,
        owner: LifecycleOwner,
        senderId: String,
        receiptId: String
    ) {
        chatModel.getAllMsg(senderId,receiptId,{
            mView.showMessages(it)
        },{
            mView.showError(it)
        })


        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        accountModel.getUseInfoById(currentUserId,{
            currentUserVO = it
           // mView.saveCurrentUserInfo(it)
        },{
           // mView.showError(it)
        })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }

    override fun onTapSendImage(img: Bitmap, msg: MessageVO) {
        msg.lastMessageSentUserName=currentUserVO?.name
        chatModel.uploadImageAndCreate(img,msg)
    }

    override fun onTapSendGifFile(file: Uri, msg: MessageVO) {
        msg.lastMessageSentUserName=currentUserVO?.name
        chatModel.uploadGifAndSendMsg(file, msg)
    }
}