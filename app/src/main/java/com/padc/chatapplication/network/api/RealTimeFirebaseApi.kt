package com.padc.chatapplication.network.api

import android.graphics.Bitmap
import android.net.Uri
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.SmallChatVO

interface RealTimeFirebaseApi {

    fun sendMessage(messageVO: MessageVO)
    fun getAllMsg(currentUserId:String,receiptId:String,onSuccess:(List<MessageVO>)->Unit,
    onFailure:(String)->Unit)
    fun uploadImageAndCreate(image: Bitmap, msg: MessageVO)
    fun getAllChatListByUserId(currentUserId: String,onSuccess:(List<SmallChatVO>)->Unit,
                               onFailure: (String) -> Unit)
    fun createNewGroup(
        img:Bitmap,
        groupVO: GroupVO, onSuccess:(String)->Unit,
                       onFailure: (String) -> Unit)


    fun getAllGroups(currentUserId:String,onSuccess:(List<GroupVO>)->Unit,
                  onFailure:(String)->Unit)

    fun sendGroupMessag(groupVO: GroupVO,msg:GroupMessageVO)
    fun getAllGroupMsg(groupId:String, onSuccess:(List<GroupMessageVO>)->Unit,
                       onFailure:(String)->Unit)
    fun uploadImageAndSendGroupMsg(img:Bitmap,groupVO: GroupVO,msg:GroupMessageVO)

    fun uploadGifAndSendMsg(file: Uri, msg: MessageVO)
    fun uploadGifAndSendGroupMsg(file: Uri,groupVO: GroupVO,msg:GroupMessageVO)
}