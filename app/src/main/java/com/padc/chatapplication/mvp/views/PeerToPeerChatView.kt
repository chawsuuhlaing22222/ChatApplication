package com.padc.chatapplication.mvp.views

import com.padc.chatapplication.data.vos.MessageVO

interface PeerToPeerChatView :BaseView{

    fun showMessages(msg:List<MessageVO>)

}