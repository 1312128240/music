package com.example.baidumusic2.bean

import android.os.Parcel
import android.os.Parcelable

data class SongListBean(
    val error_code: Int,
    val msg:String,
    val song_list: ArrayList<Song>?
)


data class Song(
        val artist_id:String,
        val pic_small:String,
        val pic_radio:String,
        val title:String,
        val author:String,
        val song_id:String,
        val artist_name:String,
        val album_title:String,
        val si_proxycompany:String,
        val language:String,
        val country:String,
        var lrclink:String,
        var isChecked:Boolean
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(artist_id)
        parcel.writeString(pic_small)
        parcel.writeString(pic_radio)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(song_id)
        parcel.writeString(artist_name)
        parcel.writeString(album_title)
        parcel.writeString(si_proxycompany)
        parcel.writeString(language)
        parcel.writeString(country)
        parcel.writeString(lrclink)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}