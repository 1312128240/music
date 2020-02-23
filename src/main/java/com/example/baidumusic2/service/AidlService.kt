package com.example.baidumusic2.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.baidumusic2.IMyAidlInterface
import com.example.baidumusic2.MyApp

class AidlService :Service() {

    override fun onCreate() {
        super.onCreate()
        println("${packageName}--->AidlService--->onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("${packageName}--->AidlService--->onStartCommand")
        return super.onStartCommand(intent, Service.START_FLAG_RETRY, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        println("${packageName}--->AidlService--->onBind")
        return MyAidlBiner()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        println("${packageName}--->AidlService--->onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("${packageName}--->AidlService--->onDestroy")
    }


    inner class MyAidlBiner:IMyAidlInterface.Stub(){
        override fun testProgress() {
            println("${packageName}--->当前时间--->${System.currentTimeMillis()}")
        }

    }
}