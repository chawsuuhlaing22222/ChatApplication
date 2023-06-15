package com.padc.chatapplication.data.vos

data class GroupMessageVO (
    var groupName:String?=null,
    var groupCoverImg:String?=null,
    var sendTimeStamp:String?=null,
    var groupId:String?=null,
    var message:String?=null,
    var senderId:String?=null,
    var senderName:String?=null,
    var senderProfile:String?=null,
    var file:String?=null,
    var fileType:String?=null

        )