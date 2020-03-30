package com.example.baidumusic2.base

import com.example.baidumusic2.MyApp

class Constant {

    companion object{

        val baseUrl:String="http://tingapi.ting.baidu.com/"

        val videoUrl:String="http://vodkgeyttp8.vod.126.net/cloudmusic/486c/core/4a5b/2d7441dc8995d408579746dc939043d8.mp4" +
                "?wsSecret=c5a3a182661c43e798c0977e32647b63&wsTime=1585124819"

        //下载文件的目录
        val downloadMusicPath="${MyApp.getContext().getExternalFilesDir(null)}/开心音乐/下载歌曲/"

        //偏好存储名字
        val MY_SP="BaiduMusic_SharedPreferences"

        val KEY_PLAY_MODE="play_mode"

        //音乐播放完成
        val ACTION_COMPLETION="com.example.baidumusic2.completion"
        //音乐列表正在播放的歌曲
        val ACTINON_NOTIFY_HOME="com.example.baidumusic2.notify"
        //准备开始播放音乐
        val ACTINON_PREPARED="com.example.baidumusic2.prepared"
        //首页旋转动画开始
        val ACTION_MAIN_ANIMATION_START="com.example.baidumusic2.main.animation.start"
        //首页旋转动画暂停
        val ACTION_MAIN_ANIMATION_PAUSE="com.example.baidumusic2.main.animation.pause"
        //首页旋转动画暂停开始
        val ACTION_MAIN_ANIMATION_RESTART="com.example.baidumusic2.main.animation.restart"

        //获取不同类型音乐
        val method_songlist="baidu.ting.billboard.billList"

        //根据id获取音乐资源
        val mehtod_play="baidu.ting.song.play"


        //下载状态
        var DOWNLOAD_INIT=-1
        val DOWNLOADING=0
        val DOWNLOAD_CANCEL=1
        val DOWNLOAD_PAUSE=2
        val DOWNLOAD_FINISH=3
        val DOWNLOAD_ERROR=4

        val ACTION_SUCCEED="com.example.baidumusic2.download.succeed"
    }
}