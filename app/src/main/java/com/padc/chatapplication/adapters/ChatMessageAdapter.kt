package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.viewholders.MessageFromMeViewHolder
import com.padc.chatapplication.viewholders.MessageFromOtherViewHolder

class ChatMessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_FROM_OTHER   =1
    val VIEW_TYPE_FROM_ME =2
    private var mData:List<MessageVO> = listOf()
    private var receiptUser:UserVO?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

          when(viewType){
              VIEW_TYPE_FROM_ME->{
                  val itemView=LayoutInflater.from(parent.context).inflate(R.layout.view_holder_msg_type_from_me,
                  parent,false)
                  return MessageFromMeViewHolder(itemView)

              }else->{
              val itemView=LayoutInflater.from(parent.context).inflate(R.layout.view_holder_chat_detail_from_other,
                  parent,false)
              return MessageFromOtherViewHolder(itemView)

          }
          }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var msg=mData[position]
        when(holder){

            is MessageFromOtherViewHolder->{
                receiptUser?.let { holder.bind(msg, it) }
            }

            is MessageFromMeViewHolder->{
                holder.bind(msg)
            }

        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        var msg=mData[position]
        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid
       return  if(msg.senderId==currentUserId){
           VIEW_TYPE_FROM_ME
        }else{
           VIEW_TYPE_FROM_OTHER
       }

    }

    fun setNewData(data:List<MessageVO>){
        mData = data
        notifyDataSetChanged()
    }
    fun setReceipt(user:UserVO){
        receiptUser=user
    }
}

