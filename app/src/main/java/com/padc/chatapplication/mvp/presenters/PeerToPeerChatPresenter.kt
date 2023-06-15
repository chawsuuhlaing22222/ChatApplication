package com.padc.chatapplication.mvp.presenters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.mvp.views.PeerToPeerChatView

interface PeerToPeerChatPresenter:BasePresenter<PeerToPeerChatView> {

    fun onTapSendMessage(message:MessageVO)
    fun onUiReady(context: Context, owner: LifecycleOwner,senderId:String,receiptId:String)
    fun onTapSendImage(img:Bitmap,msg:MessageVO)
    fun onTapSendGifFile(file: Uri,msg:MessageVO)


}