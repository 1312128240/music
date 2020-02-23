package com.example.baidumusic2.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baidumusic2.R
import com.example.baidumusic2.bean.LyricBean
import com.example.baidumusic2.tools.Screen

class LyricAdapter constructor(var mContext:Context,var lyricList:ArrayList<LyricBean>,var recy:RecyclerView)
     :RecyclerView.Adapter<LyricAdapter.LyricVh>(){

    var currentLine:Int=0;

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):LyricVh{
         if(viewType==-100){
             val headerView=View(mContext)
            // headerView.setBackgroundColor(Color.parseColor("#fa3314"))
             headerView.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,250)
             return LyricVh(headerView)
         }else if(viewType==-99){
             val emptyView=TextView(mContext)
            // emptyView.setBackgroundColor(Color.parseColor("#7BC2DB"))
             emptyView.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
             emptyView.gravity=Gravity.CENTER
             emptyView.text="抱歉,暂无歌词..."
             return LyricVh(emptyView)
         }else{
             val view=LayoutInflater.from(mContext).inflate(R.layout.item_lyric,parent,false)
             return LyricVh(view)

         }
    }

    override fun getItemCount(): Int {
       return if(lyricList.size==0) 1 else lyricList.size+1
    }


    override fun getItemViewType(position: Int): Int {
        if(lyricList.size!=0){
            return if(position==0) -100 else 0
        }
        return -99
    }

    override fun onBindViewHolder(holder:LyricVh, position: Int) {
        if(getItemViewType(position)!=-100&&getItemViewType(position)!=-99){
            val item=lyricList[position-1]
            holder.tv?.setText(item.content)
            if(position==currentLine){
                holder.tv?.setTextColor(Color.parseColor("#fa3314"))
            }else{
                holder.tv?.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    class LyricVh constructor(itemView:View):RecyclerView.ViewHolder(itemView){
        val tv=itemView.findViewById<TextView>(R.id.tv_lyric)?:null
    }


    fun notityDuration(duration:Int){
        val d=(duration/1000.0).toInt()
        for ((index,item) in lyricList.withIndex()){
            if(item.druation==d){
                notifyItemChanged(currentLine)
                currentLine=index+1
                notifyItemChanged(currentLine)
               // recy.scrollTo(0,Screen.dip2px(30f))
               // recy.scrollToPosition(currentLine)
               // recy.smoothScrollToPosition(currentLine)
                recy.smoothScrollBy(0,Screen.dip2px(30f),LinearInterpolator())
            }
        }
    }
}