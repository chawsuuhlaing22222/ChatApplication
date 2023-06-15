package com.padc.chatapplication.data.vos

data class UserVO (
    var name:String,
    var email:String,
    var phoneNo:String?,
    var profileImage:String?,
    var qrCode:String?,
    var dob:String?,
    var password:String,
    var gender:String?

        ){
    constructor():this("","",null,null,null,null,"",null)
}