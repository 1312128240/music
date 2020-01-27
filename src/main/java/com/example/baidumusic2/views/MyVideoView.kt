package com.example.baidumusic2.views

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class MyVideoView(context: Context?, attrs: AttributeSet?) : VideoView(context, attrs) {

      override fun onMeasure( widthMeasureSpec:Int,heightMeasureSpec:Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        val width = getDefaultSize(getWidth(), widthMeasureSpec);
        val height = getDefaultSize(getHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
