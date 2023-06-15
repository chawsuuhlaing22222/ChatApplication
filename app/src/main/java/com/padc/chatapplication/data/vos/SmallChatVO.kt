package com.padc.chatapplication.data.vos

data class SmallChatVO (
    var receiptUserId:String?=null,
    var msg:String?=null,
    var file:String?=null,
    var receiptVO:UserVO?=null,
    var groupVO: GroupVO?=null,
    var lastSentTimeStamp:String?=null,
    var lstSentUserName:String?=null
        )