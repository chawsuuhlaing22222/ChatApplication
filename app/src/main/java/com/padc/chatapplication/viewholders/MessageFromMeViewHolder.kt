package com.padc.chatapplication.viewholders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.utils.getDate
import kotlinx.android.synthetic.main.view_holder_msg_type_from_me.view.*

class MessageFromMeViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

    fun bind(msg:MessageVO){

        var date= msg.timeStamp?.toLong()?.let { getDate(it) }
        itemView.tvDateInChatFromMe.text=date.toString()
        Log.i("timeStamp",date.toString())

        msg.message?.let {
            itemView.tvTextFromMe.text=it
            itemView.tvTextFromMe.visibility = View.VISIBLE
        } ?: run {
            itemView.tvTextFromMe.visibility = View.GONE
        }

        msg.file?.let {
            itemView.ivImageMsgFromMe.visibility = View.VISIBLE
            if(msg.fileType=="gif"){
              //  val imageViewTarget = GlideDrawableImageViewTarget(imageView)
                Glide.with(itemView.context).asGif().load(msg.file).into(itemView.ivImageMsgFromMe)
            }else{
                Glide.with(itemView.context).load(msg.file).into(itemView.ivImageMsgFromMe)
            }

        } ?: run {
            itemView.ivImageMsgFromMe.visibility = View.GONE
        }
    }

}