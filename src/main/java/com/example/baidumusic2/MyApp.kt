package com.example.baidumusic2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.baidumusic2.download.DownloadTask
import com.example.baidumusic2.tools.ActivityManage
import com.example.baidumusic2.tools.MyCrashHandler
import com.example.baidumusic2.uis.MvActivity
import com.example.baidumusic2.uis.PlayActivity
import com.example.baidumusic2.uis.SplashActivity

class MyApp:Application(),Application.ActivityLifecycleCallbacks{

    private var activityCount:Int=0

    private var lastTime=0L

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var mContext:Context?=null

        fun getContext():Context{
            return mContext as Context
        }

        //用于标记哪个Activity进入后台,-1表示在前台
        var backFlag:Int=-1
    }


    override fun onCreate() {
        super.onCreate()
        println("Application--->onCreate")
        mContext=this
        DownloadTask.init()
        MyCrashHandler.install()
        registerActivityLifecycleCallbacks(this)
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        println("ActivityLifecycle--->${p0}---->onCreated")
    }


    override fun onActivityStarted(p0: Activity) {
        println("ActivityLifecycle--->${p0}---->onStarted")
        activityCount++
        if(backFlag==0&&System.currentTimeMillis()-lastTime>60*1000){
            startActivity(Intent(this,SplashActivity::class.java))
            backFlag=-1
        }
    }

    override fun onActivityResumed(p0: Activity) {
        println("ActivityLifecycle--->${p0}---->onResumed")
    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityStopped(p0: Activity) {
        println("ActivityLifecycle--->${p0}---->onStopped")
        activityCount--
        if(activityCount==0){
            onBackground(p0)
        }
    }


    override fun onActivityDestroyed(p0: Activity) {
        println("ActivityLifecycle--->${p0}---->onDestroyed")
        if(p0 is PlayActivity|| p0 is MainActivity) {
            ActivityManage.exit()
        }
    }

    /**
     * 从前台到后台
     */
    private fun onBackground(p0: Activity){
        println("从${p0}退到后台前")
        lastTime=System.currentTimeMillis()
        when(p0){
            is PlayActivity->{
                backFlag=1
            }
            else -> {
                backFlag=0
            }
        }
        println("返回标识onBackground--->${backFlag}")
    }
}