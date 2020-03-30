package com.example.baidumusic2.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MySwichView(context: Context?, attrs: AttributeSet?) : View(context, attrs){

    private var roundRadius=75f
    private var mPaint:Paint
    private var isChecked=false
    private var lastTimer=0L

    init {
        mPaint= Paint()
        mPaint.isAntiAlias=true
        mPaint.isFilterBitmap=true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                if(System.currentTimeMillis()-lastTimer>1000){
                    isChecked=!isChecked
                    invalidate()
                    listener?.OnChange(isChecked)
                }
                return true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL->{
                lastTimer=System.currentTimeMillis()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (isChecked){
            //绿色圆角矩形
            mPaint.style=Paint.Style.FILL
            mPaint.setColor(Color.parseColor("#1BFA1B"))
            val rectF0= RectF(0f,0f,width.toFloat(),height.toFloat())
            canvas.drawRoundRect(rectF0,roundRadius,roundRadius,mPaint)

            val size=Math.min(width,height)
            val radius=size/2f
            mPaint.setColor(Color.parseColor("#FFFFFF"))
            mPaint.style=Paint.Style.FILL

            val cx=(width-(radius-4f)*2)+(radius-4f)
            canvas.drawCircle(cx,radius,radius-4f,mPaint)
        }else{

            mPaint.style=Paint.Style.STROKE
            mPaint.strokeWidth=4f
            mPaint.setColor(Color.parseColor("#EEEEEE"))
            val rectF2= RectF(0f,0f,width.toFloat(),height.toFloat())
            canvas.drawRoundRect(rectF2,roundRadius,roundRadius,mPaint)


            val size=Math.min(width,height)
            val radius=size/2f
            mPaint.setColor(Color.parseColor("#FFFFFF"))
            mPaint.style=Paint.Style.FILL
            canvas.drawCircle(radius,radius,radius-8f,mPaint)
        }


    }


    var listener:OnSwichChangeListener?=null

    interface OnSwichChangeListener{
         fun OnChange(b:Boolean)
    }
}