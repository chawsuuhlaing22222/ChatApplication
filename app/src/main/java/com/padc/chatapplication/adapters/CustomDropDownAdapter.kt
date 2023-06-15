package com.padc.chatapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.padc.chatapplication.R

class CustomDropDownAdapter(val context: Context, var dataSource: List<Any>) : BaseAdapter(){
    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]


    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, view: View?, viewgroup: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        /*if (convertView == null) {
            view = LayoutInflater.from(viewgroup?.context).inflate(R.layout.view_item_custom_spinner_dropdown, viewgroup, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }*/
        view = LayoutInflater.from(viewgroup?.context).inflate(R.layout.view_item_custom_spinner_dropdown, viewgroup, false)
        vh = ItemHolder(view)
        view?.tag = vh
        if(position==0){
            vh.label.visibility=View.GONE
        }
        vh.label.text = dataSource.get(position).toString()

       // val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
       // vh.img.setBackgroundResource(id)

        return view
    }

}

private class ItemHolder(row: View?) {
    val label: TextView

    init {
        label = row?.findViewById(R.id.tvSpinnerSelectedValue) as TextView

    }
}
