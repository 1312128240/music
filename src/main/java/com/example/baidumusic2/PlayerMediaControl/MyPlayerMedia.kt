package com.example.baidumusic2.PlayerMediaControl

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.baidumusic2.base.Constant

class MyPlayerMedia constructor(var mContext:Context):MediaPlayer.OnPreparedListener
        ,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{

   val playerMedia by lazy { MediaPlayer() }


    init {
        playerMedia.setOnErrorListener(this)
        playerMedia.setOnPreparedListener(this)
        playerMedia.setOnCompletionListener(this)
    }

    /**
     * 播放音乐
     */
    fun  playMusic(link:String){
        playerMedia.reset()
        playerMedia.setDataSource(link)
        playerMedia.prepareAsync()
    }

    /*
     *從暫停位置播放音樂
     */
    fun restartMusic(){
        playerMedia.seekTo(playerMedia.currentPosition)
        playerMedia.start()
    }


    /**
     * 當拖動進度條時
     */
    fun dragMusic(progerss:Int){
       if(playerMedia.isPlaying){
           val position=(playerMedia.duration*((progerss*1.0)/100)).toInt()
           println("拖动位置--->${playerMedia.duration}--->${position}")
           playerMedia.seekTo(position)
           playerMedia.start()
       }
    }


    /**
     * 暂停音乐
     */
    fun pasueMusic(){
        playerMedia.pause()
    }


    /**
     * 銷燬
     */
    fun destroy(){
        playerMedia.stop()
        playerMedia.release()
    }


    /**
     * 当播放完成时
     */
    override fun onCompletion(p0: MediaPlayer?) {
        val intent=Intent("com.example.baidumusic2.broadcast.PlayBroadcastReceiver")
        intent.action=Constant.ACTION_COMPLETION
        mContext.sendBroadcast(intent)
    }


    /*
     *播放器准备就绪时开始播放
     */
    override fun onPrepared(p0: MediaPlayer?) {
        p0?.start()
        val intent=Intent("com.example.baidumusic2.broadcast.PlayBroadcastReceiver")
        intent.action=Constant.ACTINON_PREPARED
        mContext.sendBroadcast(intent)
    }

    /**
     * 如果不监听播放错误或返回false，则会调用onCompletion方法
     */
    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        return true
    }

}