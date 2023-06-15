package com.padc.chatapplication.viewholders

import android.view.View
import com.bumptech.glide.Glide
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ChatDelegate
import kotlinx.android.synthetic.main.view_holder_person.view.*

class ChatPersonViewHolder(var itemView:View,var delegate: ChatDelegate):BaseViewHolder<UserVO>(itemView) {
    override fun bindData(data: UserVO) {
        Glide.with(itemView.context).load(data.profileImage).into(itemView.ivChatPerson)
        itemView.tvNameInChat.text = data.name

        itemView.setOnClickListener {
            delegate.onTapChat(data)
        }
    }
}