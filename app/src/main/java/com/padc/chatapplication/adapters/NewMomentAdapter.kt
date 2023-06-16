package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.viewholders.MomentViewHolder

class NewMomentAdapter : ListAdapter<MomentVO, MomentViewHolder>(PostItemsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_moment_item, parent, false)
        return MomentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {


        when (holder) {
            is MomentViewHolder -> {
                holder.setIsRecyclable(false)
                holder.bindData(getItem(position))
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


private class PostItemsDiffCallback : DiffUtil.ItemCallback<MomentVO>() {

    override fun areItemsTheSame(oldItem: MomentVO, newItem: MomentVO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MomentVO, newItem: MomentVO): Boolean {
        return oldItem == newItem;
    }

}