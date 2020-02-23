package com.example.baidumusic2.tools

import com.example.baidumusic2.bean.LyricBean
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.util.regex.Pattern

object LyricResolver {

    init {

    }

    fun resolver(lyricString: String):ArrayList<LyricBean>{
        val lyricList=ArrayList<LyricBean>()
        try {
            val array =lyricString.split("\n")
            for (e in array){
                val string1=e.trim()
                if(string1.isNotEmpty()){
                    val last=string1.lastIndexOf(']',string1.length)
                    if(last!=-1){
                        val string2=string1.substring(0,last+1)
                        val string3=string1.substring(last+1,string1.length)
                        val string4=string2.replace("[","").replace("]","/")
                        val string5=string4.split("/")
                        string5.forEach(){
                            if(it.isNotBlank()){
                                val string6=it.split(":")
                                if(string6.size>1){
                                    val min=string6[0]
                                    val pattern = Pattern.compile("[0-9]*");
                                    val minIsNum = pattern.matcher(min)
                                    if(minIsNum.matches()){
                                        //分钟转换成秒
                                        val minToSecond=(min.toInt())*60
                                        //秒
                                        val string7=string6[1].split('.')
                                        val second=string7[0]
                                        val secondIsNum=pattern.matcher(second)
                                        if(secondIsNum.matches()){
                                            val totalSecond=minToSecond+(second.toInt())
                                            val bean=LyricBean(totalSecond,string3)
                                            lyricList.add(bean)
                                        }

                                    }
                                }

                            }
                        }
                    }
                }
            }
            lyricList.sortBy ({ it.druation })
        }catch (e:IndexOutOfBoundsException){
            println("解析异常-->${e}")
        }catch (e:NumberFormatException){
            println("解析异常-->${e}")
        }
        return lyricList
    }

}