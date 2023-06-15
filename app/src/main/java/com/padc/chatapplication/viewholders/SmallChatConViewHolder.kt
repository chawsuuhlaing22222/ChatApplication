package com.padc.chatapplication.viewholders

import android.view.View
import com.bumptech.glide.Glide
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.delegates.ChatDelegate
import com.padc.chatapplication.utils.getDate
import kotlinx.android.synthetic.main.view_holder_item_small_chat_conversion.view.*

class SmallChatConViewHolder(val itemView: View,var delegate: ChatDelegate): BaseViewHolder<SmallChatVO>(itemView) {
    override fun bindData(data: SmallChatVO) {

        data.receiptVO?.let {
            Glide.with(itemView.context).load(data.receiptVO?.profileImage).into(itemView.ivChatPerson)
            itemView.tvNameInChat.text=data.receiptVO?.name
        }

        data.groupVO?.let {
            Glide.with(itemView.context).load(it.groupCoverImg).into(itemView.ivChatPerson)
            itemView.tvNameInChat.text=it.groupName
        }

        data.msg?.let {
            itemView.tvLastSentTypeDes.text="${data.lstSentUserName}: $it"
        } ?: kotlin.run {
            itemView.tvLastSentTypeDes.text="${data.lstSentUserName}: file msg"
        }


        itemView.setOnClickListener {
            data.receiptVO?.let { it1 -> delegate.onTapChat(it1) }

            data.groupVO?.let {
                delegate.onTapGroupChat(it)
            }
        }

        itemView.tvLastSentDate.text= data.lastSentTimeStamp?.toLong()?.let { getDate(it) }
    }
}