package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.padc.chatapplication.data.models.AccountModel
import com.padc.chatapplication.data.models.AccountModelImpl
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.LoginPresenter
import com.padc.chatapplication.mvp.views.LoginView

class LoginPresenterImpl:LoginPresenter, AbstractBasePresenter<LoginView>() {
    private val accountModel: AccountModel = AccountModelImpl

    override fun onTapLogin(email: String, password: String) {
        accountModel.login(email,password,{
            mView.showMainPage()
        },{
            mView.showError(it)
        })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }
}