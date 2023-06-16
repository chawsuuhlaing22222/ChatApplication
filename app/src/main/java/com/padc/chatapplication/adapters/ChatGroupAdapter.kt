package com.padc.chatapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.delegates.ContactDelegate
import com.padc.chatapplication.viewholders.ChatGroupViewHolder
import kotlinx.android.synthetic.main.activity_new_group_create.view.ivGroupCoverImg
import kotlinx.android.synthetic.main.view_holder_chat_group_item.view.*

class ChatGroupAdapter(var delegate:ContactDelegate):BaseRecyclerAdapter<ChatGroupViewHolder,GroupVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatGroupViewHolder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.view_holder_chat_group_item,parent,false)
        return ChatGroupViewHolder(delegate,itemView)
    }

    override fun onBindViewHolder(holder: ChatGroupViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        var data=mData.get(position)
        Log.i("groupMsg", data.messages.toString())
        Glide.with(holder.itemView.context).load(data.groupCoverImg).into(holder.itemView.ivGroupCoverImg)
        data.groupName?.length?.let{
            if(it>6){
                holder.itemView.tvGroupName.text ="${data.groupName?.substring(0,6)}.."
            }else{
                holder.itemView.tvGroupName.text = data.groupName
            }
        }


        holder.itemView.setOnClickListener {
            delegate.tapGroup(data)
        }
    }
}