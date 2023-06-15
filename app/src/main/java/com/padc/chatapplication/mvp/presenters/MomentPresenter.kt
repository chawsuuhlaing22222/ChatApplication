package com.padc.chatapplication.mvp.presenters

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.mvp.views.MomentView

interface MomentPresenter:BasePresenter<MomentView> {

    fun onTapCreateMoment(moment: MomentVO)
    fun uploadImage(img:Bitmap)
    fun getAllMoments()
}