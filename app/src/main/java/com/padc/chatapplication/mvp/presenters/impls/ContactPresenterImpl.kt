package com.padc.chatapplication.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.data.models.*
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.AbstractBasePresenter
import com.padc.chatapplication.mvp.presenters.ContactPresenter
import com.padc.chatapplication.mvp.views.ContactView

class ContactPresenterImpl:AbstractBasePresenter<ContactView>(),ContactPresenter {

    private val accountModel: AccountModel = AccountModelImpl
    private val contactModel:ContactModel =ContactModelImpl
    private val chatModel:ChatModel= ChatModelImpl
    private var currentUserVO:UserVO?=null
    private var mSelectedMembers:MutableList<UserVO> = mutableListOf()

    override fun onTapToCreateNewGroup(img: Bitmap, group: GroupVO) {
       chatModel.createNewGroup(img,group,{
           mView.showError(it)
       },{
           mView.showError(it)
       })
    }

    override fun showSelectedGroupMember(users: List<UserVO>) {
        //mSelectedMembers= users as MutableList<UserVO>
       // mView.showSelectedGroupMember(mSelectedMembers)
    }

    override fun createContact(userId: String) {
        accountModel.getUseInfoById(userId,{

            var result=it
            currentUserVO?.let { it1 -> contactModel.createContacts(it1,it) }
        },{
            mView.showError(it)
        })
    }

    override fun onSelectContact(user: UserVO) {
        mSelectedMembers.add(user)
        mView.showSelectedGroupMember(mSelectedMembers)
    }

    override fun onUnSelectContact(user: UserVO) {
        mSelectedMembers.remove(user)
       mView.showSelectedGroupMember(mSelectedMembers)
    }

    override fun onUnSelectContactFromNewMember(user: UserVO) {
        mSelectedMembers.remove(user)
        mView.showSelectedGroupMember(mSelectedMembers)
        mView.unselectUserId()
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        accountModel.getUseInfoById(currentUserId,{
            currentUserVO = it
            mView.saveCurrentUserInfo(it)
        },{
            mView.showError(it)
        })

        //get contacts list
        contactModel.getAllContacts(currentUserId,{
          var map =it.groupBy { uservo->
              uservo.name.first()
           }
          Log.i("map","$map")

          var newmap=map.mapKeys { it->it.key.uppercaseChar() }
          mView.showContacts(newmap)
        },{
            mView.showError(it)
        })

        //get groups list
        chatModel.getAllGroupList(currentUserId,{
           var groups= it.sortedByDescending { groupVO -> groupVO.createdTimeStamp?.toLong() }
          mView.showGroups(groups)
        },{
            mView.showError(it)
        })


    }

    override fun tapContact(contact: UserVO) {
        mView.showPeerToPeerChat(contact)
    }

    override fun tapGroup(groupVO: GroupVO) {
        mView.showGroupChat(groupVO)
    }
}