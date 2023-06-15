package com.padc.chatapplication.viewholders

import android.view.View
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.delegates.ContactDelegate

class ChatGroupViewHolder(var delegate: ContactDelegate, val itemView: View): BaseViewHolder<GroupVO>(itemView) {
    override fun bindData(data: GroupVO) {

    }
}