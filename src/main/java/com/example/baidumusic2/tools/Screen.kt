package com.example.baidumusic2.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.text.Layout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.example.baidumusic2.MyApp
import java.util.*


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

        var SYSTEM_UI = 0
        @SuppressLint("NewApi")
        fun hideSystemUI(context: Context) {
            var uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }


            SYSTEM_UI = getWindow(context).getDecorView().getSystemUiVisibility()
            getWindow(context).getDecorView().setSystemUiVisibility(uiOptions)
        }

        @SuppressLint("NewApi")
        fun showSystemUI(context: Context) {
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            getWindow(context).getDecorView().setSystemUiVisibility(SYSTEM_UI)
        }

        fun getWindow(context: Context): Window {
            return if (getAppCompActivity(context) != null) {
                getAppCompActivity(context)!!.getWindow()
            } else {
               scanForActivity(context)!!.getWindow()
            }
        }

        fun scanForActivity(context: Context?): Activity? {
            if (context == null) return null
            if (context is Activity) {
                return context
            } else if (context is ContextWrapper) {
                return scanForActivity(context.baseContext)
            }

            return null
        }

        fun getAppCompActivity(context: Context?): AppCompatActivity? {
            if (context == null) return null
            if (context is AppCompatActivity) {
                return context
            } else if (context is ContextThemeWrapper) {
                return getAppCompActivity(context.baseContext)
            }
            return null
        }


        fun setRequestedOrientation(context: Context, orientation: Int) {

            if (getAppCompActivity(context) != null) {
                getAppCompActivity(context)?.setRequestedOrientation(orientation)
            } else {
                scanForActivity(context)?.setRequestedOrientation(orientation)
            }
        }


        @SuppressLint("RestrictedApi")
        fun showStatusBar(context: Context) {
            getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //如果是沉浸式的，全屏前就没有状态栏
        @SuppressLint("RestrictedApi")
        fun hideStatusBar(context: Context) {
            getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
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


        //生成指定区间随机数
         fun randomInt(min: Int, max: Int): Int {
            return Random().nextInt(max) % (max - min + 1) + min
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


        /**
         * 检查悬浮权限
         */
        fun  checkFloatPermission(context:Context):Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                return true;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                try {
                    var cls = Class.forName("android.content.Context");
                    val declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                    declaredField.setAccessible(true);
                    var obj = declaredField.get(cls);
                    if (!(obj is String)) {
                        return false;
                    }
                    val str2 = obj as String;
                    obj = cls.getMethod("getSystemService", String.javaClass).invoke(context, str2);
                    cls = Class.forName("android.app.AppOpsManager");
                    val declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                    declaredField2.setAccessible(true);
                    val checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.javaClass);
                    val result =  checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                    return result == declaredField2.getInt(cls);
                } catch (e:Exception) {
                    return false;
                }
            } else {
                return Settings.canDrawOverlays(context);
            }
        }

    }




}