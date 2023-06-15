package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.viewholders.GroupMsgFromOtherViewHolder
import com.padc.chatapplication.viewholders.MessageFromMeViewHolder

class GroupChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_FROM_OTHER   =1
    val VIEW_TYPE_FROM_ME =2
    private var mData:List<GroupMessageVO> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType){
            VIEW_TYPE_FROM_ME->{
                val itemView= LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_msg_type_from_me,
                    parent,false)
                return MessageFromMeViewHolder(itemView)

            }else->{
            val itemView= LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_chat_detail_from_other,
                parent,false)
            return GroupMsgFromOtherViewHolder(itemView)

        }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var groupMsg=mData[position]
        var msg=MessageVO(groupMsg.sendTimeStamp,groupMsg.message,groupMsg.senderId,null,
        groupMsg.file,null)

        when(holder){

            is GroupMsgFromOtherViewHolder ->{
              holder.bind(groupMsg)
            }

            is MessageFromMeViewHolder ->{
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

    fun setNewData(data:List<GroupMessageVO>){
        mData = data
        notifyDataSetChanged()
    }

}