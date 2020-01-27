package com.example.baidumusic2.bean

import com.example.baidumusic2.adapter.MvAdapter

data class VideoBean (
        //id
       var id:Int=0,
       //url
       var path:String="",
       //0表示开始,1表示暂停，2暂停继续播放,3播放完成，4，即将开始下一首
       var status:Int=0,
       //当前播放时间
       var currentduration:Int=0,
        //歌曲总时间
       var totalduration:Int=962946,
       //viewHolder
       var beanHolder:MvAdapter.VideoViewHolder?=null
)
