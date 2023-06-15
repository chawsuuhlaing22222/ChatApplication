package com.padc.chatapplication.mvp.presenters

import com.padc.chatapplication.mvp.views.LoginView

interface LoginPresenter:BasePresenter<LoginView> {

    fun onTapLogin(email:String,password:String)
}