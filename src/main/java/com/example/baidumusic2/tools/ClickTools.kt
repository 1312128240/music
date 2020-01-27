package com.example.baidumusic2.tools

private var lastClickTimer:Long=0

fun isNotQuickClick():Boolean{

    if(System.currentTimeMillis()- lastClickTimer>1000){
        lastClickTimer=System.currentTimeMillis()
        return  true
    }
    lastClickTimer=System.currentTimeMillis()
    return false
}