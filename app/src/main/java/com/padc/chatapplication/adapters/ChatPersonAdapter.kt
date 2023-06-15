package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ChatDelegate
import com.padc.chatapplication.viewholders.ChatPersonViewHolder

class ChatPersonAdapter(var delegate: ChatDelegate):BaseRecyclerAdapter<ChatPersonViewHolder,UserVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatPersonViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.view_holder_person,parent,false)
        return ChatPersonViewHolder(itemView,delegate)
    }

    override fun onBindViewHolder(holder: ChatPersonViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindData(mData[position])
    }
}