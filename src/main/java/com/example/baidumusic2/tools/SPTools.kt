package com.example.baidumusic2.tools

import android.content.Context
import android.content.SharedPreferences
import android.graphics.ColorSpace
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.base.Constant

object SPTools {

    private  var  sp: SharedPreferences?=null

    init {
       sp=MyApp.getContext().getSharedPreferences(Constant.MY_SP,Context.MODE_PRIVATE)
    }

    fun save(key:String,vaule:String){
        sp?.edit()?.putString(key,vaule)?.commit()
    }

    fun get(key: String):String?{
       return sp?.getString(key,"1")
    }
}