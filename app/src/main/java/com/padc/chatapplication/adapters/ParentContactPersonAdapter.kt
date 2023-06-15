package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import com.padc.chatapplication.viewholders.ParentContactPersonViewHolder

class ParentContactPersonAdapter(var delegate: ContactDelegate):BaseRecyclerAdapter<ParentContactPersonViewHolder,Pair<Char,List<UserVO>>>() {
    private var flag_check=0
    private var selectedUserIds:List<String> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentContactPersonViewHolder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.view_holder_paent_contact_person,parent,false)
        return ParentContactPersonViewHolder(delegate,itemView,flag_check,selectedUserIds)
    }

    override fun onBindViewHolder(holder: ParentContactPersonViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
       // var map:Map<Char,String> = mapOf('k' to "K",'c' to "C")
       // var map1 = map.get(map.keys.elementAt(0))

        holder.bindData(mData[position])
    }

    fun setFlagCheck(){
        flag_check=1
    }

    /*fun setSelectedUserIds(userIds: List<String>){
        selectedUserIds=userIds
        notifyDataSetChanged()
    }*/
}