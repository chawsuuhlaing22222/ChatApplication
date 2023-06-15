package com.padc.chatapplication.data.vos

data class MessageVO (

    var timeStamp:String?=null,
    var message:String?=null,
    var senderId:String?=null,
    var receiptId:String?=null,
    var file:String?=null,
    var lastMessageSentUserName:String?=null,
    var fileType:String?=null

        )