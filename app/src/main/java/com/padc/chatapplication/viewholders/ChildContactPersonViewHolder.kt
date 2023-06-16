package com.padc.chatapplication.viewholders

import android.view.View
import com.bumptech.glide.Glide
import com.padc.chatapplication.R
import com.padc.chatapplication.activities.NewGroupCreateActivity
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import kotlinx.android.synthetic.main.view_holder_child_contact_person.view.*

class ChildContactPersonViewHolder(var delegate: ContactDelegate, val itemView: View, var flag:Int,var selectedUserIds:List<String>): BaseViewHolder<UserVO>(itemView) {

    override fun bindData(data: UserVO) {
      if(flag==1){
          itemView.checkToAddGroupChat.visibility=View.VISIBLE
      }else{
          itemView.checkToAddGroupChat.visibility=View.GONE
      }

        Glide.with(itemView.context).load(data.profileImage).into(itemView.ivContactPerson)
        itemView.tvNameInContact.text =  data.name

        itemView.setOnClickListener {
            delegate.tapContact(data)
        }

        itemView.checkToAddGroupChat.setOnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked){

                if(NewGroupCreateActivity.selectedUserList.contains(data.qrCode)){
                    //itemView.checkToAddGroupChat.isChecked=true
                    delegate.onUnSelectContact(data)
                    itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.uncheck_fill)
                }else{
                    delegate.onSelectContact(data)
                    itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.check_fill)

                }
            }else{
                delegate.onUnSelectContact(data)
                itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.uncheck_fill)
            }

        }

        data.qrCode?.let {
            if(NewGroupCreateActivity.selectedUserList.contains(it)){
               //itemView.checkToAddGroupChat.isChecked=true
              itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.check_fill)
            }else{
                itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.uncheck_fill)

            }
        }

        /*if(!itemView.checkToAddGroupChat.isChecked){
            itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.uncheck_fill)
        }else{
            itemView.checkToAddGroupChat.setButtonDrawable(R.drawable.check_fill)
        }*/
    }
}