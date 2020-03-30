package com.example.baidumusic2.bean

import com.example.baidumusic2.adapter.MvAdapter

data class VideoBean (
        //id
       var id:Int=0,
       //url
       var path:String="",
       //0表示重头开始播放,1表示暂停,2播放错误,4播放中
       var status:Int=0,
       //缩略图
       var thurmb:String="",
       //当前播放时间
       var currentduration:Int=0,
        //歌曲总时间
       var totalduration:Int=962946

)
