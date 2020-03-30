package com.example.baidumusic2.room

import android.os.Parcel
import android.os.Parcelable
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
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readByte() != 0.toByte(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readParcelable(DownloadEntity::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(songId)
        parcel.writeString(name)
        parcel.writeByte(if (isCollect) 1 else 0)
        parcel.writeString(lrcLink)
        parcel.writeString(lrcContent)
        parcel.writeString(pic_big)
        parcel.writeString(pic_small)
        parcel.writeParcelable(downloadEntity,flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicEntity> {
        override fun createFromParcel(parcel: Parcel): MusicEntity {
            return MusicEntity(parcel)
        }

        override fun newArray(size: Int): Array<MusicEntity?> {
            return arrayOfNulls(size)
        }
    }
}


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
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(downloadId)
        parcel.writeString(title)
        parcel.writeString(fileLink)
        parcel.writeString(author)
        parcel.writeString(fileExtension)
        parcel.writeInt(fileSize)
        parcel.writeInt(duration)
        parcel.writeInt(status)
        parcel.writeInt(progress)
        parcel.writeInt(startPoistion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadEntity> {
        override fun createFromParcel(parcel: Parcel): DownloadEntity {
            return DownloadEntity(parcel)
        }

        override fun newArray(size: Int): Array<DownloadEntity?> {
            return arrayOfNulls(size)
        }
    }
}