package com.example.baidumusic2.adapter

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.baidumusic2.R
import com.example.baidumusic2.tools.Screen

class VideoAdapter constructor(var mContext:Activity,var lists:ArrayList<String>): BaseAdapter() {

    private var isLand=false

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view=LayoutInflater.from(mContext).inflate(R.layout.item_mv,p2,false)
        return view
    }

    fun getPortraitlp():LinearLayout.LayoutParams{
        val height=Screen.dip2px(360f)
        return LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height)
    }

    fun getLandscapelp():LinearLayout.LayoutParams{
        var stateBarHeight=Screen.getStatusBarHeight()
        val width=Screen.getWidthPixel()
        val height=Screen.getHeightPixel()
        return LinearLayout.LayoutParams(width,height-stateBarHeight)
    }

    override fun getItem(p0: Int): String{
       return  lists.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
         return  lists.size
    }

     class MyViewHolder{

     }
}