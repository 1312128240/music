package com.example.baidumusic2.tools

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.service.PlayService
import java.lang.ref.WeakReference

object ActivityManage{


    private var activityMap=HashMap<String,WeakReference<AppCompatActivity>>()

    fun getActivityMap():HashMap<String,WeakReference<AppCompatActivity>>{
        return activityMap
    }

    /**
     * 添加Activity
     */
   fun add(activity:AppCompatActivity){
        val reference= WeakReference<AppCompatActivity>(activity)
        activityMap.put(activity.localClassName.toString(),reference)
    }


    /**
     * 移除Activity
     */
    fun remove(activity: AppCompatActivity){
        activityMap.remove(activity.localClassName.toString())
    }


    /**
     * 结束所有Activity
     */
    fun finishAll(){
        val iterator = activityMap.iterator()
        while (iterator.hasNext()){
            val vaule=iterator.next()
            val activity = vaule.value.get()
            if(activity!=null){
                println("任务栈移除--->${activity}")
                activity.finish()
                iterator.remove()
            }
        }
        activityMap.clear()
    }


    /**
     * 退出
     */
   fun exit(){
        finishAll()
        MyCrashHandler.unInstall()
        MyApp.getContext().stopService(Intent(MyApp.getContext(),PlayService::class.java))
        System.exit(1)
    }
}