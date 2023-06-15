package com.padc.chatapplication

import com.padc.chatapplication.utils.ConfigUtils

class ChatApplication: android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        ConfigUtils.initConfigUtils(this)
    }
}