package com.padc.chatapplication.data.vos

data class MomentVO(
    var id:String?,
    var userName:String?,
    var userProfile:String?,
    var description:String?,
    var images:String?,
    var uploadDate:String?,
    var uploadUserId:String?,
){
    constructor():this(null,"","",null,null,null,"")
}