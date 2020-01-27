package com.example.baidumusic2.animation

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

class ViewRotateAnimation constructor(var view: View) {

    private var rotateAnimation: ObjectAnimator?=null

   init {
        initAnimation()
    }

   private fun initAnimation(){
        rotateAnimation= ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotateAnimation?.let {
            it.repeatCount=-1
            it.duration=12000
            it.interpolator= LinearInterpolator()
        }
    }

    fun startAnimation(){
        rotateAnimation?.start()
    }

    fun restartAnimation(){
        rotateAnimation?.resume()
    }


    fun pauseAnimation(){
        rotateAnimation?.pause()
    }

    fun endAnimation(){
        rotateAnimation?.end()
    }
}