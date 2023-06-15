package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.network.api.FirebaseApi
import com.padc.grocery.network.auth.AuthManager

interface AccountModel {
    var mAuthManager: AuthManager
    var firebaseApi:FirebaseApi
    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun register(email: String, password: String, userName: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun getUserName() : String

    fun getUseInfoById(userid:String,onSuccess: (userVO:UserVO) -> Unit, onFailure: (String) -> Unit)
    fun createUser(userVO: UserVO)
    fun uploadImageAndCreateUser(img:Bitmap,userVO: UserVO,onSuccess: () -> Unit, onFailure: (String) -> Unit)



}