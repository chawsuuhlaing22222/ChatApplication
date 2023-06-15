package com.padc.chatapplication.mvp.presenters

import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.views.QRScannerView

interface QRScannerPrsenter:BasePresenter<QRScannerView> {

    fun getUserInfoById(
        userId: String
    )

    fun onSaveCurrentUser(userVO: UserVO)
}