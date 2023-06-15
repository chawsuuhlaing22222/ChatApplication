package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padc.chatapplication.R
import com.padc.chatapplication.viewholders.ChatMsgTypeFromOtherViewHolder

class ChatMsgTypeFromOtherAdapter: RecyclerView.Adapter<ChatMsgTypeFromOtherViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatMsgTypeFromOtherViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_msg_type_from_other
        ,parent,false)
        return ChatMsgTypeFromOtherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatMsgTypeFromOtherViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
     return 2
    }
}