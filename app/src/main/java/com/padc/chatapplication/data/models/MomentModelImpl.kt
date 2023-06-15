package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.network.api.CloudFireStoreApiImpl
import com.padc.chatapplication.network.api.FirebaseApi

object MomentModelImpl :MomentModel{
    override var firebaseApi: FirebaseApi = CloudFireStoreApiImpl

    override fun onTapCreateMoment(moment: MomentVO) {
       firebaseApi.createMoment(moment)
    }

    override fun uploadImage(img: Bitmap, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
       firebaseApi.uploadImage(img,onSuccess, onFailure = onFailure)
    }

    override fun getAllMoments(onSuccess: (List<MomentVO>) -> Unit, onFailure: (String) -> Unit) {
        firebaseApi.getAllMoments(onSuccess, onFailure)
    }

    override fun getMyMoments(
        userId: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseApi.getMyMoments(userId,onSuccess,onFailure)
    }
}