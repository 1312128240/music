package com.example.baidumusic2.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.VideoView

class MyVideoView(context: Context?, attrs: AttributeSet?) : VideoView(context, attrs) {

    private var myRunnable:Runnable?=null
    private var listener:OnVideoViewListener?=null
    private var isShow=false

    init {
        myRunnable= Runnable {
            if(isShow){
                println("动作--->隐藏")
                isShow=false
                listener?.onTouchCancel()
            }
        }
    }


    fun setListner(l:OnVideoViewListener){
        this.listener=l
    }

    fun removeRunnable(){
        removeCallbacks(myRunnable)
        isShow=false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action){
            MotionEvent.ACTION_DOWN->{
                if(isShow){
                   removeCallbacks(myRunnable)
                }else{
                    isShow=true
                    listener?.onTouchDown()
                    println("动作--->显示")
                }
                return true
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP->{
                if(isShow){
                    postDelayed(myRunnable,2500)
                }
            }
        }

        return super.onTouchEvent(ev)
    }

    interface OnVideoViewListener{
        fun onTouchDown()
        fun onTouchCancel()
    }
}