package com.padc.chatapplication.viewholders

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.padc.chatapplication.adapters.ChildContactPersonAdapter
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.delegates.ContactDelegate
import kotlinx.android.synthetic.main.view_holder_paent_contact_person.view.*

class ParentContactPersonViewHolder(var delegate: ContactDelegate,val itemView: View,var flag:Int,var selectedUserIds:List<String>): BaseViewHolder<Pair<Char,List<UserVO>>>(itemView) {


    override fun bindData(data: Pair<Char,List<UserVO>>) {

        var childContactAdapter = ChildContactPersonAdapter(delegate)
        if(flag==1){
            childContactAdapter.setFlagCheck()
        }

        //childContactAdapter.setSelectedUserIds(selectedUserIds)

        var key=data.first
        data.second?.let { childContactAdapter.setNewData(it) }

        itemView.tvStartAlphabetOfContact.text = key.toString()

        var itemDecoration =object :  RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top=0
                outRect.top=20
            }
        }


        if( itemView.rvContactPerson.itemDecorationCount>0){
            itemView.rvContactPerson.removeItemDecoration(itemDecoration)
        }else{
            itemView.rvContactPerson.addItemDecoration(itemDecoration)
        }

        itemView.rvContactPerson.apply {
            adapter=childContactAdapter
            layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)


        }


    }


    }

    private fun setUpRecycler() {

}