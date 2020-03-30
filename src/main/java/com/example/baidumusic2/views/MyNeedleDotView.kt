package com.example.baidumusic2.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyNeedleDotView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val cx=width/2
        val cy=height/2
        //外圆
        val paint1=Paint()
        paint1.setColor(Color.parseColor("#22000000"))
        canvas?.drawCircle(cx.toFloat(),cy.toFloat(),cx.toFloat(),paint1)

        val paint2=Paint()
        paint2.setColor(Color.parseColor("#FFFFFF"))
        canvas?.drawCircle(cx.toFloat(),cy.toFloat(),(cx-13).toFloat(),paint2)

        val paint3=Paint()
        paint3.setColor(Color.parseColor("#22000000"))
        canvas?.drawCircle(cx.toFloat(),cy.toFloat(),12f,paint3)

    }
}