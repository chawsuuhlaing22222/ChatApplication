package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.viewholders.PostImageViewHolder

class PostImageAdapter:BaseRecyclerAdapter<PostImageViewHolder,String>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImageViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.view_holder_post_image,parent,false)
        return PostImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindData(mData.get(position))
    }
}