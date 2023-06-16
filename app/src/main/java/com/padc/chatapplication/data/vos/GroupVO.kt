package com.padc.chatapplication.data.vos

data class GroupVO(

    var groupName:String?=null,
    var members:String?=null,
    var groupCoverImg:String?=null,
    var createdTimeStamp:String?=null,
    var messages:MutableMap<String,GroupMessageVO>?=null,
    var memberCount:Int?=0,
    var lastMessage:String?=null,
    var lastMessageSentUserName:String?=null,
    var lastSendUserId:String?=null

)

//var messages:Map<String,List<GroupMessageVO>>?=null,
