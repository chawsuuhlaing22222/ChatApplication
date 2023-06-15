package com.padc.chatapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.padc.chatapplication.R
import com.padc.chatapplication.viewholders.AlphabetViewHolder
import kotlinx.android.synthetic.main.view_holder_alphabet.view.*

class AlphabetAdapter:BaseRecyclerAdapter<AlphabetViewHolder,String>() {

    private var currentChar:Char?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetViewHolder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.view_holder_alphabet,parent,false)
        return AlphabetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlphabetViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        if(position==0){
            holder.itemView.tvAlphabet.visibility=View.GONE
            holder.itemView.ivStar.visibility=View.VISIBLE
        }else{

            var data=mData.get(position).first().toChar()

           currentChar?.let {
               if(data.equals(it,true)){

                   holder.itemView.tvAlphabet.setTextColor(
                       holder.itemView.resources.getColor(R.color.green,null))
                  }
            }
            holder.itemView.tvAlphabet.text=mData.get(position)
            holder.itemView.tvAlphabet.visibility=View.VISIBLE
        }

    }

    fun setCurrentChar(char:Char){
        currentChar=char
        notifyDataSetChanged()
    }

}