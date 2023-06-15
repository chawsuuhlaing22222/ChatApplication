package com.padc.chatapplication.mvp.views

import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.data.vos.UserVO

interface ChatView:BaseView {

    fun showContacts(contacts:List<UserVO>)
    fun showShortChatList(chats:List<SmallChatVO>)
    fun showPeerToPeerChatConversation(receipt:UserVO)
    fun saveGroupList(groupList:List<GroupVO>)
    fun showGroupChat(groupVO: GroupVO)
}