package com.padc.chatapplication.viewholders

import android.view.View
import com.bumptech.glide.Glide
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import kotlinx.android.synthetic.main.view_holder_new_group_member.view.*

class NewGroupMemberViewHOlder(val itemView: View,var delegate:ContactDelegate): BaseViewHolder<UserVO>(itemView) {
    override fun bindData(data: UserVO) {
      Glide.with(itemView.context).load(data.profileImage).into(itemView.ivNewMemberCoverImg)
        itemView.tvPersonNameInNewGroup.text=data.name

        itemView.ivRemove.setOnClickListener {
            delegate.onUnSelectContact(data)
        }
    }
}