package com.padc.chatapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.utils.getDate
import kotlinx.android.synthetic.main.view_holder_chat_detail_from_other.view.*

class MessageFromOtherViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    fun bind(msg:MessageVO,receiptUser:UserVO) {
       /* var mAdapter=ChatMsgTypeFromOtherAdapter()
        itemView.rvMsgFromOther.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL,false)

        }*/

        var date= msg.timeStamp?.toLong()?.let { getDate(it) }
        Glide.with(itemView.context).load(receiptUser.profileImage).into(itemView.ivPersonFromOther)

        msg.message?.let {
            itemView.tvTextFromOther.text=it
            itemView.llTextMsgFromOther.visibility = View.VISIBLE
            itemView.tvDateFromOtherForText.text=date.toString()
        } ?: run {
            itemView.llTextMsgFromOther.visibility = View.GONE
        }

        msg.file?.let {

            itemView.llImageMsgFromOther.visibility=View.VISIBLE
            itemView.tvDateFromOtherForImg.text=date.toString()
            if(msg.fileType=="gif"){
                //  val imageViewTarget = GlideDrawableImageViewTarget(imageView)
                Glide.with(itemView.context).asGif().load(msg.file).into(itemView.ivImageMsgFromOther)
            }else{
                Glide.with(itemView.context).load(msg.file).into(itemView.ivImageMsgFromOther)
            }
        } ?: run {
            itemView.llImageMsgFromOther.visibility= View.GONE
        }
    }


}