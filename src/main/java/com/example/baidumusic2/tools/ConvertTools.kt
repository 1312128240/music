package com.example.baidumusic2.tools

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.provider.MediaStore
import java.util.HashMap

class ConvertTools {

    companion object{

        fun  timeParse(duration:Int): String{
            var time = "" ;
            val minute = duration/1000/60 ;
            val seconds = (duration/1000) % 60 ;
            if( minute < 10 ){
                time += "0" ;
            }
            time +="${ minute}:" ;
            if( seconds < 10 ){
                time += "0" ;
            }
            time += seconds ;
            return time ;
        }


        /**
         * 时间毫秒值转为进度
         */
        fun timeToProgress(currentDuration:Int,totalDuration:Int):Int{
           return (currentDuration*100.0/totalDuration).toInt()
        }


        /**
         * 获取网络视频缩略图片
         */
       fun createVideoThumbnail(url: String, width: Int, height: Int): Bitmap? {
            var bitmap: Bitmap? = null
            val retriever = MediaMetadataRetriever()
            val kind = MediaStore.Video.Thumbnails.MINI_KIND
            try {
                retriever.setDataSource(url, HashMap())
                bitmap = retriever.frameAtTime
            } catch (ex: IllegalArgumentException) {
                // Assume this is a corrupt video file
            } catch (ex: RuntimeException) {
                // Assume this is a corrupt video file.
            } finally {
                try {
                    retriever.release()
                } catch (ex: RuntimeException) {
                    // Ignore failures while cleaning up.
                }
            }
            if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
            }
            return bitmap
        }
    }
}