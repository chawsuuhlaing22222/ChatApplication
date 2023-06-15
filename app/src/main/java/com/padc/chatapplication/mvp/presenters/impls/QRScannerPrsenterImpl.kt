package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.AccountModel
import com.padc.chatapplication.data.models.AccountModelImpl
import com.padc.chatapplication.data.models.ContactModel
import com.padc.chatapplication.data.models.ContactModelImpl
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.QRScannerPrsenter
import com.padc.chatapplication.mvp.views.QRScannerView

class QRScannerPrsenterImpl:AbstractBasePresenter<QRScannerView>(),QRScannerPrsenter {

    private val accountModel: AccountModel = AccountModelImpl
    private val contactModel: ContactModel = ContactModelImpl
    private var currentUserVO:UserVO?=null
    var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    override fun getUserInfoById(
        userId: String
    ) {
        accountModel.getUseInfoById(userId,{

             var result=it
            currentUserVO?.let { it1 -> contactModel.createContacts(it1,it) }
        },{
            mView.showError(it)
        })
    }

    override fun onSaveCurrentUser(userVO: UserVO) {
        currentUserVO=userVO
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }
}