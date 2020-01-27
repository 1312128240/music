package com.example.baidumusic2.tools

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.baidumusic2.MyApp


class Screen {

    companion object {

        /**
         * 6.0设置状态栏样式
         */
        fun setStatusBar(activity: Activity, isTransparent: Boolean, isDarkText: Boolean, @ColorRes bgColor: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                val decorView = window.decorView
                decorView.systemUiVisibility = if (isTransparent) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else 0 or
                        if (isDarkText) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
                window.statusBarColor = if (isTransparent) Color.TRANSPARENT else ContextCompat.getColor(activity, bgColor) //Android5.0就可以用
            }
        }


        /**
         * 获取状态栏高度
         */
        fun getStatusBarHeight(): Int {
            val resources = MyApp.getContext().resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }


        /**
         * 获取屏幕宽像素
         */
        fun getWidthPixel(): Int {
            val windowManager = MyApp.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.getDefaultDisplay().getMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels
            return widthPixels
        }

        /**
         * 获取屏幕高像素
         */
        fun getHeightPixel(): Int {
            val windowManager = MyApp.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.getDefaultDisplay().getMetrics(outMetrics)
            val heightPixels = outMetrics.heightPixels
            return heightPixels
        }


        //dp转px
        fun dip2px(dpValue: Float): Int {
            val scale = MyApp.getContext().getResources().getDisplayMetrics().density
            return (dpValue * scale + 0.5f).toInt()
        }


        /**
         * 设置Activity背景颜色
         */
        fun setBackgroundAlpha(activity: AppCompatActivity, bgAlpha: Float) {
            val lp = activity.getWindow().getAttributes();
            lp.alpha = bgAlpha;
            (activity).getWindow().setAttributes(lp);
        }


        /**
         * 横竖屏切换
         *
         */
        fun setChangeScreen(activity: Activity,isLandscape:Boolean){
            if(isLandscape){
                //横屏
                if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }else{
                //竖屏
                if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        }

    }

}