package com.padc.chatapplication.mvp.presenters

import android.graphics.Bitmap
import android.net.Uri
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.mvp.views.GroupChatView

interface GroupChatPresenter:BasePresenter<GroupChatView> {

    fun sendGroupChatMessage(groupVO: GroupVO,msg:GroupMessageVO)
    fun getGroupMsg(groupId:String)
    fun uploadImageAndSendGroupMsg(img: Bitmap, groupVO: GroupVO, msg:GroupMessageVO)
    fun uploadGifFileAndSendGroupMsg(file: Uri, groupVO: GroupVO, msg:GroupMessageVO)
}