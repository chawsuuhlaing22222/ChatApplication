package com.padc.chatapplication.delegates

import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO

interface ChatDelegate {
    fun onTapChat(receipt:UserVO)
    fun onTapGroupChat(receipt: GroupVO)
}