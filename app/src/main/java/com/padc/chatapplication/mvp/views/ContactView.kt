package com.padc.chatapplication.mvp.views

import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO

interface ContactView:BaseView {

    fun saveCurrentUserInfo(userVO: UserVO)
    fun showContacts(contacts:Map<Char,List<UserVO>>)
    fun showPeerToPeerChat(receipt:UserVO)
    fun showGroups(groups:List<GroupVO>)
    fun showSelectedGroupMember(users:List<UserVO>)
    fun unselectUserId()
    fun showGroupChat(groupVO: GroupVO)
}