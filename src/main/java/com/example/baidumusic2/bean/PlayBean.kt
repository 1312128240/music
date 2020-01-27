package com.example.baidumusic2.bean

import android.os.Parcel
import android.os.Parcelable

data class PlayBean(
    val bitrate: Bitrate?,
    val error_code: Int,
    var msg:String,
    val songinfo: Songinfo?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Bitrate::class.java.classLoader)!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readParcelable(Songinfo::class.java.classLoader)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(bitrate, flags)
        parcel.writeInt(error_code)
        parcel.writeString(msg)
        parcel.writeParcelable(songinfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayBean> {
        override fun createFromParcel(parcel: Parcel): PlayBean {
            return PlayBean(parcel)
        }

        override fun newArray(size: Int): Array<PlayBean?> {
            return arrayOfNulls(size)
        }
    }
}

data class Bitrate(
    val file_bitrate: Int,
    val file_duration: Int,
    val file_extension: String,
    val file_link: String,
    val file_size: Int,
    val free: Int,
    val hash: String,
    val show_link: String,
    val song_file_id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(file_bitrate)
        parcel.writeInt(file_duration)
        parcel.writeString(file_extension)
        parcel.writeString(file_link)
        parcel.writeInt(file_size)
        parcel.writeInt(free)
        parcel.writeString(hash)
        parcel.writeString(show_link)
        parcel.writeInt(song_file_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bitrate> {
        override fun createFromParcel(parcel: Parcel): Bitrate {
            return Bitrate(parcel)
        }

        override fun newArray(size: Int): Array<Bitrate?> {
            return arrayOfNulls(size)
        }
    }
}

class Songinfo(
        val title: String,
        val song_id: String,
        val pic_big: String,
        val pic_small: String,
        val lrclink: String,
        val author: String,
        val album_title:String,
        val si_proxycompany:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(song_id)
        parcel.writeString(pic_big)
        parcel.writeString(pic_small)
        parcel.writeString(lrclink)
        parcel.writeString(author)
        parcel.writeString(album_title)
        parcel.writeString(si_proxycompany)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Songinfo> {
        override fun createFromParcel(parcel: Parcel): Songinfo {
            return Songinfo(parcel)
        }

        override fun newArray(size: Int): Array<Songinfo?> {
            return arrayOfNulls(size)
        }
    }
}