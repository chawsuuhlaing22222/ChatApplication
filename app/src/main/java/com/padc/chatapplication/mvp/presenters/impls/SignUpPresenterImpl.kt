package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import com.padc.chatapplication.data.models.AccountModel
import com.padc.chatapplication.data.models.AccountModelImpl
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.SignUpPresenter
import com.padc.chatapplication.mvp.views.SignUpView

class SignUpPresenterImpl: AbstractBasePresenter<SignUpView>(), SignUpPresenter {

    private val accountModel:AccountModel = AccountModelImpl
    override fun onTapSignUp(
        img: Bitmap,
        userVO: UserVO,

    ) {

        var error=""
      accountModel.register(userVO.email,userVO.password,userVO.name,
          {

             accountModel.uploadImageAndCreateUser(img,userVO,
                 {
                   mView.showLogin()
                 }, {
                    error=it
                 })
              mView.showLogin()
          }, {
              mView.showError("${it} and $error")
          })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }


}