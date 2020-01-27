package com.example.baidumusic2.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import com.example.baidumusic2.tools.Screen

class DiscTranAnimation constructor(var mContext: Context, var view: View) {


     fun leftExitAnimation(){
        val width= Screen.getWidthPixel().toFloat()
        val tranExitAnimation=ObjectAnimator.ofFloat(view,"translationX",0f,-width)
        val tranEnterAnimation=ObjectAnimator.ofFloat(view,"translationX", width,0f)
        val set=AnimatorSet()
       // set?.playTogether(tranExitAnimation,tranEnterAnimation)
       // set?.play(tranEnterAnimation)?.after(tranExitAnimation)
        set.playSequentially(tranExitAnimation,tranEnterAnimation)
        set.duration=1000
        set.start()
    }



    fun rightExitAnimation(){
        val width= Screen.getWidthPixel().toFloat()
        val tranExitAnimation=ObjectAnimator.ofFloat(view,"translationX",0f,width)
        val tranEnterAnimation=ObjectAnimator.ofFloat(view,"translationX", -width,0f)
        val set=AnimatorSet()
        // set?.playTogether(tranExitAnimation,tranEnterAnimation)
        // set?.play(tranEnterAnimation)?.after(tranExitAnimation)
        set.playSequentially(tranExitAnimation,tranEnterAnimation)
        set.duration=1000
        set.start()
    }

}