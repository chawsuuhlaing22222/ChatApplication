package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import com.padc.chatapplication.viewholders.ChildContactPersonViewHolder

class ChildContactPersonAdapter(var delegate:ContactDelegate):BaseRecyclerAdapter<ChildContactPersonViewHolder,UserVO>() {

    private var flag_check=0
    private var selectedUserIds:List<String> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildContactPersonViewHolder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.view_holder_child_contact_person,parent,false)
        return ChildContactPersonViewHolder(delegate,itemView,flag_check,selectedUserIds)
    }

    override fun onBindViewHolder(holder: ChildContactPersonViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        var data=mData.get(position)
        holder.bindData(data)


    }


    fun setFlagCheck(){
        flag_check=1
    }



    fun setSelectedUserIds(selectedUserIds: List<String>) {
        this.selectedUserIds =selectedUserIds
        notifyDataSetChanged()
    }


}