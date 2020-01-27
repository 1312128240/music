package com.example.baidumusic2.tools

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

class StringTools{

    companion object{

        fun downloadText(textUrl:String):String{
            try {
                val url=URL(textUrl)
                val openConnection = url.openConnection() as HttpURLConnection
                val input=openConnection.inputStream
                val sb=StringBuilder()
                val buffer=ByteArray(8*1024)
                var readerLength=input.read(buffer)
                do {
                    val string= String(buffer,0,readerLength)
                    sb.append(string)
                    readerLength=input.read(buffer)
                }while (readerLength!=-1)
                input.close()
                return sb.toString()
            }catch (e:IOException){
               println("下载异常${e.toString()}")
            }
            return "歌词下载失败..."
        }

        fun toM(byte:Int):String{
            val d=byte*1.0/1024/1024
            val nf = DecimalFormat("#0.00")
            return nf.format(d)
        }
    }


}