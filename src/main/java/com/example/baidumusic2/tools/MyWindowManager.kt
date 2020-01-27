package com.example.baidumusic2.tools

import android.annotation.SuppressLint
import android.graphics.PixelFormat
import android.media.Image
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.baidumusic2.MainActivity
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.R
import kotlinx.android.synthetic.main.activity_play.*
import android.R.attr.gravity
import android.view.WindowManager.LayoutParams.*


@SuppressLint("StaticFieldLeak")
class MyWindowManager {

     fun showWindow(activity: AppCompatActivity){

         val layout=LayoutInflater.from(MyApp.getContext()).inflate(R.layout.view_float_window,null)

         //获取LayoutParams对象
         val wmParams = WindowManager.LayoutParams()

         //获取的是LocalWindowManager对象
         val mWindowManager = activity.getWindowManager()
         //window的类型
         wmParams.type = WindowManager.LayoutParams.TYPE_PHONE
         //设置图片格式，效果为背景透明
         wmParams.format = PixelFormat.RGBA_8888
         /**
          *
         FLAG_NOT_FOCUSABLE
         表示window不需要获取焦点，这个比较好理解，设置了这个属性后，window将不会获取到焦点
         FLAG_NOT_TOUCH_MODAL
         在此模式下，系统会将当前Window区域以外的单击事件传递给底层的Window，当前Window区域以内的单击事件则自己处理。 一般来说都需要开启此标记，否则其他Window将无法收到单击事件
         FLAG_SHOW_WHEN_LOCKED
         此模式可以让Window显示在锁屏的界面上
          */
         wmParams.flags = FLAG_NOT_FOCUSABLE
         wmParams.gravity = Gravity.LEFT or Gravity.TOP
         //wmParams.x = 0
        // wmParams.y = 0
         wmParams.width = WindowManager.LayoutParams.MATCH_PARENT
         wmParams.height =320
         mWindowManager.addView(layout,wmParams);

         layout.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
             mWindowManager.removeView(layout)
         }
     }

}