package com.example.baidumusic2.tools

import android.widget.Toast
import com.example.baidumusic2.MyApp

class MyToast {

    companion object{
        fun short(content:String){
            Toast.makeText(MyApp.getContext(),content,Toast.LENGTH_SHORT).show()
        }
    }

}