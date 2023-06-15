package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.SmallChatVO
import com.padc.chatapplication.delegates.ChatDelegate
import com.padc.chatapplication.viewholders.SmallChatConViewHolder

class SmallChatConversationAdapter(var delegate:ChatDelegate):BaseRecyclerAdapter<SmallChatConViewHolder,SmallChatVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallChatConViewHolder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.view_holder_item_small_chat_conversion,parent,false)
        return SmallChatConViewHolder(itemView,delegate)
    }

    override fun onBindViewHolder(holder: SmallChatConViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindData(mData.get(position))
    }
}