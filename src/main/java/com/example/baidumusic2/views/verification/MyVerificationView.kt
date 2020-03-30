package com.example.baidumusic2.views.verification

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.baidumusic2.tools.Screen

class MyVerificationView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var slideWidth=150f
    private var paint1=Paint()
    private val paint2=Paint()
    private val paint3=Paint()

    private var paddingLeft=0f

    private val path1=Path()

    private var bingo=false

    init {
        paint1.setColor(Color.parseColor("#CBCBCB"))

        paint2.strokeWidth=2f
        paint2.style=Paint.Style.STROKE
        paint2.setColor(Color.WHITE)

        paint3.setColor(Color.GREEN)
    }

    private var downX=0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                if(event.x<=slideWidth&&!bingo){
                    println("在区域内")
                    downX=event.x
                    return true
                }else{
                    println("不在区域内")
                }
            }

            MotionEvent.ACTION_MOVE->{
                val moveX=event.x-downX
                println("ActionMove--->${width}--->${moveX}")
                paddingLeft=moveX
                if(paddingLeft>0){
                    if((paddingLeft+slideWidth)<width&&!bingo){
                        //postInvalidate()
                        invalidate()
                        listener?.onDraging(moveX)
                    }else{
                        println("Action最右边")
                        bingo=true
                    }
                }else{
                    println("Action最左边")
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL->{
                println("action--->取消")
                listener?.onDragCancel()
                if(!bingo){
                    downX=0f
                    bingo=false
                    paddingLeft=0f
                    //postInvalidate()
                    invalidate()
                }else{
                    postDelayed(object :Runnable{
                        override fun run() {
                            downX=0f
                            bingo=false
                            paddingLeft=0f
                            //postInvalidate()
                            invalidate()
                        }
                    },2000)
                }

            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.drawRect(0f,0f,paddingLeft,height.toFloat(),paint3)

        //滑块宽
        canvas.drawRect(paddingLeft,0f,slideWidth+paddingLeft,height.toFloat(),paint1)
        //2个箭头
        path1.reset()//清除之前绘制的path.
        path1.moveTo((slideWidth/3)+paddingLeft,(height/3).toFloat())
        path1.lineTo((slideWidth/2)+paddingLeft,(height/2).toFloat())
        path1.lineTo((slideWidth/3)+paddingLeft,((height*2)/3).toFloat())
        canvas.drawPath(path1,paint2)
//
        path1.moveTo((slideWidth/2)+paddingLeft,(height/3).toFloat())
        path1.lineTo((slideWidth*2/3)+paddingLeft,(height/2).toFloat())
        path1.lineTo((slideWidth/2)+paddingLeft,((height*2)/3).toFloat())
        canvas.drawPath(path1,paint2)

        val string="请按住滑动,拖到最右边"
        val stringSize=Screen.dip2px(14f).toFloat()
        paint1.textSize=stringSize
        val stringWidth=(string.length-1)*stringSize
        canvas.drawText(string,(width/2f)-(stringWidth/2),(height/2f)+(stringSize/2),paint1)
        canvas.save()
    }

  var listener:OnDragListener?=null


  interface OnDragListener{
      fun onDraging(distance:Float)

      fun onDragCancel()
  }

}