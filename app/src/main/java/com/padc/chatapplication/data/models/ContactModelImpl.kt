package com.padc.chatapplication.data.models

import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.network.api.CloudFireStoreApiImpl
import com.padc.chatapplication.network.api.FirebaseApi

object ContactModelImpl :ContactModel{

    override var firebaseApi: FirebaseApi=CloudFireStoreApiImpl

    override fun createContacts(userVO: UserVO, contactVO: UserVO) {
        firebaseApi.createContacts(userVO,contactVO)
    }

    override fun getAllContacts(
        userid: String,
        onSuccess: (List<UserVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseApi.getMyContacts(userid,onSuccess,onFailure)
    }
}