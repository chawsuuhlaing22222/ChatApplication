package com.padc.chatapplication.data.models

import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.network.api.FirebaseApi

interface ContactModel {
    var firebaseApi: FirebaseApi
    fun createContacts(userVO: UserVO, contactVO: UserVO)
    fun getAllContacts(userid:String,onSuccess:(List<UserVO>)->Unit,onFailure:(String)->Unit)
}