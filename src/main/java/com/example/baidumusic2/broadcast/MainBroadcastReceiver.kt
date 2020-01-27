package com.example.baidumusic2.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.baidumusic2.MainActivity

class MainBroadcastReceiver constructor(var activity:MainActivity):BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let{
            val pic=it.getStringExtra("pic")!!
            val action=it.action!!
            activity.bottomAmiation(pic,action)
         }
    }
}