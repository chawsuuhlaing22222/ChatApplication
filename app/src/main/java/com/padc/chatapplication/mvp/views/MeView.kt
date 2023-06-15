package com.padc.chatapplication.mvp.views

import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO

interface MeView:BaseView {

    fun showUserInfo(userVO: UserVO)
    fun showMyMoments(moments:List<MomentVO>)
}