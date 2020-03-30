package com.example.baidumusic2.views

import android.content.Context
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircleImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private var mPaint=Paint()
    private var mRadius:Float=40f


    override fun onDraw(canvas: Canvas) {
        if (drawable is BitmapDrawable) {
            val cx=(width / 2).toFloat()
            val cy=(height / 2).toFloat()
            mRadius=(width / 2).toFloat()
            val bitmap=(drawable as BitmapDrawable).bitmap
            val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            mPaint.setShader(bitmapShader)
            canvas.drawCircle(cx,cy, mRadius, mPaint);

            return;
        }
        super.onDraw(canvas);
    }


}