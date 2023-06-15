package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.viewholders.PostAddedImgViewHolder

class PostAddedImgAdapter:BaseRecyclerAdapter<PostAddedImgViewHolder,String>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAddedImgViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.view_holder_added_imgs,parent,false)
        return PostAddedImgViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostAddedImgViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.bindData(mData.get(position))
    }


}