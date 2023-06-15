package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.AccountModel
import com.padc.chatapplication.data.models.AccountModelImpl
import com.padc.chatapplication.data.models.MomentModelImpl
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.MePresenter
import com.padc.chatapplication.mvp.views.MeView

class MePresenterImpl:AbstractBasePresenter<MeView>(),MePresenter {

    private val accountModel: AccountModel = AccountModelImpl
    private val momentModel =MomentModelImpl
    override fun updateUserInfo(userVO: UserVO) {
      accountModel.createUser(userVO)
    }

    override fun updateImage(img: Bitmap, userVO: UserVO) {
        accountModel.uploadImageAndCreateUser(img,userVO,
            {
                mView.showError("Successfully edited")
            }, {
               mView.showError(it)
            })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        var currentUserId =FirebaseAuth.getInstance().currentUser?.uid ?: ""
        accountModel.getUseInfoById(currentUserId,{
            mView.showUserInfo(it)
        },{
            mView.showError(it)
        })

        //get my moments

        currentUserId?.let { it ->
            momentModel.getMyMoments(it,{ moments->
               mView.showMyMoments(moments)
            },{
                mView.showError(it)
            })
        }


    }
}