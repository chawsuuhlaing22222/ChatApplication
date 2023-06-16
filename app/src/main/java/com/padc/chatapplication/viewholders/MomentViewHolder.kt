package com.padc.chatapplication.viewholders

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padc.chatapplication.adapters.PostImageAdapter
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.utils.getDate
import kotlinx.android.synthetic.main.view_holder_moment_item.view.*


class MomentViewHolder(var itemView:View):RecyclerView.ViewHolder(itemView) {

     fun bindData(data: MomentVO) {
        //rvImages
        var postImageAdapter=PostImageAdapter()
        var imageList:List<String> = listOf()
        if(!data.images.isNullOrEmpty()){
             imageList= data.images?.split(",") as List<String>
        }

      /*  if(imageList.isNullOrEmpty()){
            itemView.rvMomentImg.visibility=View.GONE
        }else{*/
        if(!imageList.isNullOrEmpty()){
            itemView.rvMomentImg.apply {
                adapter=postImageAdapter
                imageList?.let { postImageAdapter.setNewData(it) }
                if(imageList.count()!=1){
                    layoutManager = GridLayoutManager(itemView.context,2)

                    addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            super.getItemOffsets(outRect, view, parent, state)
                            outRect.left = 10
                            outRect.top=10
                        }
                    })
                }else{
                    layoutManager =
                        LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL,false)

                }

            }
        }else{
            itemView.rvMomentImg.visibility=View.GONE
        }



        itemView.tvMomentDes.text=data.description
        Glide.with(itemView.context).load(data.userProfile).into(itemView.ivProfileInMoment)
        itemView.tvName.text = data.userName
         Log.i("date",data.uploadDate.toString())
        itemView.tvMomentDate.text= data.uploadDate?.toLong()?.let { getDate(it) }


    }
}