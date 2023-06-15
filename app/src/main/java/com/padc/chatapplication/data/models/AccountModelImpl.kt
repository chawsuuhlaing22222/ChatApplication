package com.padc.chatapplication.data.models

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.network.api.CloudFireStoreApiImpl
import com.padc.chatapplication.network.api.FirebaseApi
import com.padc.grocery.network.auth.AuthManager
import com.padc.grocery.network.auth.FirebaseAuthManager

object AccountModelImpl :AccountModel {

    override var mAuthManager: AuthManager=FirebaseAuthManager
    override var firebaseApi: FirebaseApi= CloudFireStoreApiImpl

    override fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuthManager.login(email, password, onSuccess, onFailure)
    }

    override fun register(
        email: String,
        password: String,
        userName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuthManager.register(email, password, userName, onSuccess, onFailure)
    }

    override fun getUserName(): String {
      return  mAuthManager.getUserName()
    }

    override fun getUseInfoById(userid:String,
        onSuccess: (userVO: UserVO) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseApi.retrieveUserInfo(userid,onSuccess, onFailure)
    }

    override fun createUser(userVO: UserVO) {
         firebaseApi.createUser(userVO)
    }

    override fun uploadImageAndCreateUser(
        img: Bitmap,
        userVO: UserVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseApi.uploadImageAndCreateUser(img,userVO, onSuccess, onFailure)
    }
}