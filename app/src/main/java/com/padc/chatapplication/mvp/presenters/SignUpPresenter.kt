package com.padc.chatapplication.mvp.presenters

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.views.SignUpView

interface SignUpPresenter:BasePresenter<SignUpView> {

    fun onTapSignUp(img:Bitmap,userVO: UserVO)


}