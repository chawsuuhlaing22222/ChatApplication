package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.AccountModel
import com.padc.chatapplication.data.models.AccountModelImpl
import com.padc.chatapplication.data.models.MomentModelImpl
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.MomentPresenter
import com.padc.chatapplication.mvp.views.MomentView

class MomentPresenterImpl : AbstractBasePresenter<MomentView>(),MomentPresenter{

    private var momentModel = MomentModelImpl
    private val accountModel: AccountModel = AccountModelImpl

    override fun onTapCreateMoment(moment: MomentVO) {
       momentModel.onTapCreateMoment(moment)
    }

    override fun uploadImage(img: Bitmap) {
        momentModel.uploadImage(img,
            {
              mView.saveImageUrl(it)
            }, {
                 mView.showError(it)
            })
    }

    override fun getAllMoments() {
        momentModel.getAllMoments({
            mView.showAllMoments(it)
        },{
            mView.showError(it)
        })


    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
      accountModel.getUseInfoById(currentUserId,{
          mView.saveCurrentUserInfo(it)
      },{
          mView.showError(it)
      })

        momentModel.getAllMoments({
            mView.showAllMoments(it)
        },{
            mView.showError(it)
        })


    }
}