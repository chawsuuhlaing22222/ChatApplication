package com.padc.chatapplication.viewholders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_holder_added_imgs.view.*

class PostAddedImgViewHolder(var itemView:View):BaseViewHolder<String>(itemView) {
    override fun bindData(data: String) {

        Glide.with(itemView.context).load(data).into(itemView.ivPostimg)
    }
}