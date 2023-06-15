package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.network.api.FirebaseApi

interface MomentModel {
    var firebaseApi: FirebaseApi
    fun onTapCreateMoment(moment: MomentVO)
    fun uploadImage(img: Bitmap, onSuccess:(String)->Unit, onFailure:(String)->Unit)
    fun getAllMoments(onSuccess:(List<MomentVO>)->Unit,onFailure: (String) -> Unit)
    fun getMyMoments(userId:String,onSuccess:(List<MomentVO>)->Unit,onFailure: (String) -> Unit)
}