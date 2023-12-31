package com.padc.chatapplication.mvp.presenters

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.padc.chatapplication.mvp.views.BaseView


abstract class AbstractBasePresenter<T: BaseView> : BasePresenter<T>, ViewModel() {

    protected lateinit var mView : T

    override fun initPresenter(view: T){
        mView = view
    }

   /* fun sendEventsToFirebaseAnalytics(
        context: Context,
        eventName: String,
        key: String = "",
        value: String = ""
    ) {
        if(key.isNotEmpty() && value.isNotEmpty()){
            FirebaseAnalytics.getInstance(context).logEvent(eventName, buildBundle(key, value))
        } else {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, null)
        }
    }
*/
    private fun buildBundle(key: String, value: String): Bundle {
        val bundle = Bundle()
        bundle.putString(key, value)
        return bundle
    }
}