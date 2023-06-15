package com.padc.chatapplication.mvp.views

import com.padc.chatapplication.data.vos.GroupMessageVO

interface GroupChatView:BaseView {

    fun showGroupChatMsg(msg:List<GroupMessageVO>)
}