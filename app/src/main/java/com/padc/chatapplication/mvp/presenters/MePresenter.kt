package com.padc.chatapplication.mvp.presenters

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.views.MeView

interface MePresenter :BasePresenter<MeView>{
    fun updateUserInfo(userVO: UserVO)
    fun updateImage(img: Bitmap, userVO: UserVO)

}