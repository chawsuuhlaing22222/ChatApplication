package com.padc.chatapplication.mvp.presenters

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import com.padc.chatapplication.mvp.views.ContactView

interface ContactPresenter:BasePresenter<ContactView>,ContactDelegate {

    fun onTapToCreateNewGroup(img: Bitmap, group:GroupVO)

    fun showSelectedGroupMember(users: List<UserVO>)

    fun createContact(userId:String)
}