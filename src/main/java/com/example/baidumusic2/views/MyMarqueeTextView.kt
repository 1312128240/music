package com.example.baidumusic2.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class MyMarqueeTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {


    override fun isFocused(): Boolean {
        return true
    }


}