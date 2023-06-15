package com.padc.chatapplication.mvp.presenters

import com.padc.chatapplication.delegates.ChatDelegate
import com.padc.chatapplication.mvp.views.ChatView

interface ChatPresenter:BasePresenter<ChatView>,ChatDelegate {

}