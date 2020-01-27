package com.example.baidumusic2.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.baidumusic2.MyApp

 class MyKeyDownReceiver: BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            when(p1.action){
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS->{
                    val reason =it.getStringExtra("reason")

                    println("按下的是--->${reason}")
                  //  println("按下的是1---》${reason}--->${MyApp.backFlag}")
                }
            }
        }
    }

}
