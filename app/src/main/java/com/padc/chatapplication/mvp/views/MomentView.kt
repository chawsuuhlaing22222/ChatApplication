package com.padc.chatapplication.mvp.views

import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO

interface MomentView:BaseView {

    fun saveImageUrl(img:String)
    fun saveCurrentUserInfo(userVO: UserVO)
    fun showAllMoments(moments:List<MomentVO>)
}