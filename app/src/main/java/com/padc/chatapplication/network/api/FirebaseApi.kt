package com.padc.chatapplication.network.api

import android.graphics.Bitmap
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO

interface FirebaseApi {

  //account
  fun retrieveUserInfo(userId:String,onSuccess:(userVO:UserVO)->Unit,onFailure:(String)->Unit)
  fun createUser(userVO: UserVO)
  fun uploadImageAndCreateUser(image : Bitmap, userVO: UserVO,onSuccess:()->Unit,
                               onFailure:(String)->Unit)

  //contacts
  fun createContacts(userVO: UserVO, contactVO:UserVO)
  fun getMyContacts(userid:String,onSuccess:(List<UserVO>)->Unit,onFailure:(String)->Unit)

  //moments
  fun createMoment(momentVO: MomentVO)
  fun uploadImage(image : Bitmap,onSuccess:(String)->Unit,
    onFailure:(String)->Unit)
  fun getAllMoments(onSuccess: (List<MomentVO>) -> Unit,onFailure: (String) -> Unit)
  fun getMyMoments(userId: String, onSuccess: (List<MomentVO>) -> Unit, onFailure: (String) -> Unit)
}