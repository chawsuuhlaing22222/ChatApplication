package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import android.net.Uri
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.network.api.CloudFireStoreApiImpl
import com.padc.chatapplication.network.api.FirebaseApi
import com.padc.chatapplication.network.api.RealTimeFirebaseApi
import com.padc.chatapplication.network.api.RealtimeDatabaseFirebaseApiImpl

object GroupChatModelImpl:GroupChatModel {
    override var firebaseApi: FirebaseApi= CloudFireStoreApiImpl

    override var realTimeApi: RealTimeFirebaseApi = RealtimeDatabaseFirebaseApiImpl


    override fun sendMessage(groupVO: GroupVO, msg: GroupMessageVO) {
      realTimeApi.sendGroupMessag(groupVO,msg)
    }

    override fun getAllGroupMsg(
        groupId: String,
        onSuccess: (List<GroupMessageVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
       realTimeApi.getAllGroupMsg(groupId, onSuccess, onFailure)
    }

    override fun uploadImageAndSendGroupMsg(img: Bitmap,groupVO: GroupVO, msg: GroupMessageVO) {
       realTimeApi.uploadImageAndSendGroupMsg(img,groupVO,msg)
    }

    override fun uploadGifAndSendGroupMsg(file: Uri, groupVO: GroupVO, msg: GroupMessageVO) {
        realTimeApi.uploadGifAndSendGroupMsg(file, groupVO, msg)
    }
}