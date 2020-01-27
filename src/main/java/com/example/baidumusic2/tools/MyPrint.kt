package com.example.baidumusic2.tools

import android.os.Build
import com.example.baidumusic2.BuildConfig

object MyPrint {

    fun pln(string: String){
        if(BuildConfig.DEBUG){
            println(string)
        }
    }
}