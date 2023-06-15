package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import android.net.Uri
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.network.api.FirebaseApi
import com.padc.chatapplication.network.api.RealTimeFirebaseApi

interface GroupChatModel {
    var firebaseApi: FirebaseApi
    var realTimeApi: RealTimeFirebaseApi
    fun sendMessage(groupVO: GroupVO, msg: GroupMessageVO)
    fun getAllGroupMsg(groupId:String, onSuccess:(List<GroupMessageVO>)->Unit,
                  onFailure:(String)->Unit)

    fun uploadImageAndSendGroupMsg(img: Bitmap, groupVO: GroupVO,msg:GroupMessageVO)
    fun uploadGifAndSendGroupMsg(file: Uri, groupVO: GroupVO, msg:GroupMessageVO)
}