package com.example.baidumusic2.views.verification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.baidumusic2.R


class MyVerificationLayout(context: Context?, attrs: AttributeSet) : LinearLayout(context, attrs){


    init {
           val view=LayoutInflater.from(context).inflate(R.layout.layout_verification,this,false)
           addView(view)

           val imageView=view.findViewById<MyImageVerificationView>(R.id.imageView)
           val verificationView=view.findViewById<MyVerificationView>(R.id.VerificationView)

            verificationView.listener=object :MyVerificationView.OnDragListener{
                override fun onDragCancel() {
                     imageView.setStop()
                }

                override fun onDraging(distance: Float) {
                     imageView.setLeftPadding(distance)
                }

            }
       }



}