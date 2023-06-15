package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import com.padc.chatapplication.viewholders.NewGroupMemberViewHOlder
import kotlinx.android.synthetic.main.view_holder_new_group_member.view.*

class NewGroupMemberAdapter(var delegate:ContactDelegate):BaseRecyclerAdapter<NewGroupMemberViewHOlder,UserVO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewGroupMemberViewHOlder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.view_holder_new_group_member,parent,false)
        return NewGroupMemberViewHOlder(itemView,delegate)
    }

    override fun onBindViewHolder(holder: NewGroupMemberViewHOlder, position: Int) {
        super.onBindViewHolder(holder, position)

        var data =mData.get(position)
        //holder.bindData(user)

        Glide.with(holder.itemView.context).load(data.profileImage).into(holder.itemView.ivNewMemberCoverImg)
        holder.itemView.tvPersonNameInNewGroup.text=data.name

        holder.itemView.ivRemove.setOnClickListener {
            delegate.onUnSelectContactFromNewMember(data)
        }


    }


}