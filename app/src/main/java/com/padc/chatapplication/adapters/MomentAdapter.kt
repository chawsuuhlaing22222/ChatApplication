package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.viewholders.MomentViewHolder


class MomentAdapter:BaseRecyclerAdapter<MomentViewHolder,MomentVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.view_holder_moment_item,parent,false)
        return MomentViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindData(mData.get(position))
    }
}