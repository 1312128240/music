package com.example.baidumusic2.uis

import android.content.Intent
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.example.baidumusic2.MainActivity
import com.example.baidumusic2.R
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.tools.Screen

class SplashActivity :MyBaseActivity<com.example.baidumusic2.databinding.ActivitySplashBinding>() {

    private val myCountDownTimer by lazy { MyCountDownTimer() }

    private val alpha by lazy {   AlphaAnimation(0.5f,1f) }

    override fun getLayoutId(): Int {
        Screen.setStatusBar(this,true,false,R.color.colorWhite)
        return R.layout.activity_splash
    }

    override fun business() {
        myCountDownTimer.start()
        startAnmiation()
    }

    private fun startAnmiation(){
        alpha.duration=5*1000
        dataBinding?.tvCountDown?.animation=alpha
        alpha.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if(!isFinishing){
                    startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                    finish()
                }
            }

            override fun onAnimationStart(p0: Animation?) {

            }

        })
    }



    inner class MyCountDownTimer:CountDownTimer(5*1000,1000){
        override fun onFinish() {
            cancel()
        }

        override fun onTick(p0: Long) {
            dataBinding?.tvCountDown?.text="倒计时 ${(p0/1000)-1}"
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        alpha.cancel()
        myCountDownTimer.cancel()
    }

}
