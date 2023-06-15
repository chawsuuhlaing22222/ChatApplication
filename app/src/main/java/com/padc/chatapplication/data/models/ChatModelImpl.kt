package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import android.net.Uri
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.network.api.CloudFireStoreApiImpl
import com.padc.chatapplication.network.api.FirebaseApi
import com.padc.chatapplication.network.api.RealTimeFirebaseApi
import com.padc.chatapplication.network.api.RealtimeDatabaseFirebaseApiImpl

object ChatModelImpl:ChatModel {

    override var firebaseApi: FirebaseApi=CloudFireStoreApiImpl
    override var realTimeApi: RealTimeFirebaseApi=RealtimeDatabaseFirebaseApiImpl

    override fun sendMessage(msg: MessageVO) {
        realTimeApi.sendMessage(msg)
    }

    override fun getAllMsg(
        currentUserId: String,
        receiptId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        realTimeApi.getAllMsg(currentUserId, receiptId, onSuccess, onFailure)
    }

    override fun uploadImageAndCreate(image: Bitmap, msg: MessageVO) {
        realTimeApi.uploadImageAndCreate(image, msg)
    }

    override fun getAllChatListByUserId(
        currentUserId: String,
        onSuccess: (List<SmallChatVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
       realTimeApi.getAllChatListByUserId(currentUserId, onSuccess, onFailure)
    }

    override fun createNewGroup(
        img:Bitmap,
        groupVO: GroupVO,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        realTimeApi.createNewGroup(img,groupVO, onSuccess, onFailure)
    }

    override fun getAllGroupList(currentUserId:String,onSuccess: (List<GroupVO>) -> Unit, onFailure: (String) -> Unit) {
        realTimeApi.getAllGroups(currentUserId, onSuccess, onFailure)
    }

    override fun uploadGifAndSendMsg(file: Uri, msg: MessageVO) {
        realTimeApi.uploadGifAndSendMsg(file, msg)
    }
}