package com.example.baidumusic2.room

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "Music", indices = [Index("id")])
data class MusicEntity (
    @PrimaryKey
    @ColumnInfo(name="id")
    var songId:String="",
    //歌曲名字
    @NonNull
    var name:String="",
    //是否收藏过
    @NonNull
    var isCollect:Boolean=false,
    //歌词
    @NonNull
    var lrcLink:String="",
    @NonNull
    var lrcContent:String="",
     //大图片
    @NonNull
    var pic_big:String="",
     //小图片
    @NonNull
    var pic_small:String="",
    //嵌入entity
    @Embedded
    var downloadEntity:DownloadEntity?=null
)


@Entity(tableName = "Download")
data class DownloadEntity(
        @PrimaryKey
        var downloadId:String="",
        // 歌曲名字
        @NonNull
        var title:String="",
        //歌曲链接
        @NonNull
        var fileLink:String="",
        //歌曲作者
        @NonNull
        var author:String="",
        //歌曲后缀
        @NonNull
        var fileExtension:String="mp3",
        //歌曲文件大小
        @NonNull
        var fileSize:Int=0,
        //歌曲时长
        @NonNull
        var duration:Int=0,
        //下载的状态已完成，下载中
        @NonNull
        var status:Int=0,
        //下载进度
        @NonNull
        var progress:Int=0,
        //下载开始位置
        @NonNull
        var startPoistion:Int=0
)