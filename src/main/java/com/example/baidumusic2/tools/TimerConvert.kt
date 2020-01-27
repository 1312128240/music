package com.example.baidumusic2.tools

class TimerConvert {

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

    }
}