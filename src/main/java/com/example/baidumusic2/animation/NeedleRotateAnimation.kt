package com.example.baidumusic2.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.*
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.R

class NeedleRotateAnimation constructor(var mContext:Context,var view: View){


   private var needleStartAnimation:Animation?=null
   private var needleStopAnimation: Animation?=null

    init {
        initAnimation()
    }

    private fun initAnimation() {
       // needleStartAnimation = AnimationUtils.loadAnimation(MyApp.getContext(), R.anim.role_needle_start)
        needleStartAnimation = RotateAnimation(0f,-30f)
        needleStartAnimation?.duration=1000
        needleStartAnimation?.fillAfter=true

        needleStopAnimation= AnimationUtils.loadAnimation(mContext, R.anim.role_needle_pause)
    }

    fun startAnimation(){
        view.startAnimation(needleStartAnimation)
    }

    fun stopAnimation(){
        view.startAnimation(needleStopAnimation)
    }


}