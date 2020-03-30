package com.example.baidumusic2.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.example.baidumusic2.tools.Screen

class MyDiskBgView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width=Screen.getWidthPixel()*1.0/1.6
        val height=width
        val lp=FrameLayout.LayoutParams(width.toInt()-230,height.toInt()-230)
        lp.gravity=Gravity.CENTER
         for(i in 0 until childCount){
             val childView=getChildAt(i)
             childView.layoutParams=lp
         }
        setMeasuredDimension(width.toInt(), height.toInt())


    }
}