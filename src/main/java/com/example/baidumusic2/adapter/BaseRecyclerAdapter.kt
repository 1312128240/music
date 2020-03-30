package com.example.baidumusic2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T>(var mContext:Context,var dataLists:ArrayList<T>,var layout:Int)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER_VIEW=0;
    private val ITEM_VIEW=1;
    var listener:OnItemClickListener<T>?=null

    var headerView:View?=null
        set(value) {field=value}
        get() {return field};

    override fun getItemViewType(position: Int): Int {
        return if (headerView!=null && position <1) {
            //头部View
            0
        }else {
            //内容View
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        if(viewType==0&&headerView!=null){
            return HeaderViewHolder(headerView!!)
        }
        val view=LayoutInflater.from(mContext).inflate(layout,parent,false)
        return ContentViewHolder(view)
    }


    override fun getItemCount(): Int {
        if(headerView!=null){
            return dataLists.size+1
        }
        return dataLists.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ContentViewHolder){
            if(headerView!=null){
                bindViewHoler(holder,position,dataLists[position-1])
                holder.view.setOnClickListener {
                    listener?.OnClick(position-1,dataLists[position-1],dataLists)
                }

            }else{
                bindViewHoler(holder,position,dataLists[position])
                holder.view.setOnClickListener {
                    listener?.OnClick(position,dataLists[position],dataLists)
                }
            }


        }
    }


    abstract fun bindViewHoler(holder: ContentViewHolder, position: Int, t:T);


    class ContentViewHolder(var view: View):RecyclerView.ViewHolder(view){

        fun setText(id:Int,content:String){
           view.findViewById<TextView>(id)?.text=content
        }

        fun<T:View> getView(id:Int):T?{
            return view.findViewById(id)
        }
    }

    class HeaderViewHolder(var view: View):RecyclerView.ViewHolder(view){

    }


    //添加 数据方法
    fun add(list:List<T>,isClean:Boolean){
        if(isClean){
            dataLists.clear()
        }
        dataLists.addAll(list)
        notifyDataSetChanged()
    }

    fun add(bean:T){
        dataLists.add(bean)
        notifyItemInserted(dataLists.size-1)
    }

    fun remove(position: Int){
        dataLists.removeAt(position)
        notifyItemRemoved(position)
    }

    //获取数据源
    fun getDatas():ArrayList<T>{
        return dataLists
    }


    //点击
    interface OnItemClickListener<T>{
        fun OnClick(position:Int,bean:T,lists:ArrayList<T>)
    }
}