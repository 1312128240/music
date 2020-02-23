package com.example.baidumusic2.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.baidumusic2.IMyAidlInterface
import com.example.baidumusic2.PlayerMediaControl.MyPlayerMedia

class PlayService:Service() {

    private val myPlayerMedia by lazy { MyPlayerMedia(this) }


    override fun onCreate() {
        super.onCreate()
        println("playService--->onCreate")
    }


    /*
     *  START_STICKY 会重建并重并且intent对象的值被清除了
     *  START_NOT_STICKY 不会重建
     *  START_STICKY_COMPATIBILITY会onCreata重建,但不会onStart重启
     *  START_REDELIVER_INTENT 默认的 会重建并重启服务并保留intent
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("playService--->onStartCommand")
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        println("playService--->onBind")
        return MyBinder()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        println("playService--->onUnbind")
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        myPlayerMedia.destroy()
        println("playService--->onDestroy")
    }

    /**
     * 用于本应用与本进程通信
     */
    inner class MyBinder:Binder(){

        fun start(link:String){
            myPlayerMedia.playMusic(link)
        }

        fun pause(){
            myPlayerMedia.pasueMusic()
        }

        fun restart(){
            myPlayerMedia.restartMusic()
        }

        fun drag(progerss:Int){
            myPlayerMedia.dragMusic(progerss)
        }


        fun getMediaPlayer(): MediaPlayer?{
            return myPlayerMedia.playerMedia
        }

    }

}