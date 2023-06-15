package com.padc.chatapplication.delegates

import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO

interface ContactDelegate {
    fun tapContact(contact: UserVO)
    fun tapGroup(groupVO: GroupVO)
    fun onSelectContact(user:UserVO)
    fun onUnSelectContact(user: UserVO)
    fun onUnSelectContactFromNewMember(user: UserVO)
}