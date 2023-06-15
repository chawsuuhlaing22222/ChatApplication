package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import android.net.Uri
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.network.api.FirebaseApi
import com.padc.chatapplication.network.api.RealTimeFirebaseApi

interface ChatModel {
    var firebaseApi: FirebaseApi
    var realTimeApi:RealTimeFirebaseApi
    fun sendMessage(msg:MessageVO)
    fun getAllMsg(currentUserId:String,receiptId:String,onSuccess:(List<MessageVO>)->Unit,
                  onFailure:(String)->Unit)
    fun uploadImageAndCreate(image: Bitmap, msg: MessageVO)
    fun getAllChatListByUserId(currentUserId: String, onSuccess:(List<SmallChatVO>)->Unit,
                               onFailure: (String) -> Unit)


    fun createNewGroup(img:Bitmap,
                       groupVO: GroupVO, onSuccess:(String)->Unit,
                       onFailure: (String) -> Unit)

    fun getAllGroupList(currentUserId:String,onSuccess:(List<GroupVO>)->Unit,  onFailure: (String) -> Unit)

    //gif
    fun uploadGifAndSendMsg(file: Uri, msg: MessageVO)
}