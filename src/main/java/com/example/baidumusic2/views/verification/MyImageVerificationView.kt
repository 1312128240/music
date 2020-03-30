package com.example.baidumusic2.views.verification

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.Toast
import com.example.baidumusic2.tools.Screen
import java.util.*

class MyImageVerificationView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs){


    private var shadowPain=Paint()
    private var bitmapPaint=Paint()

    private var shadowPath= Path()
    private var shadowSize=0f
    private var shadowSmallSize=36f
    private var shadowLeft=0
    private var blockLeft=0f
    private var topY=0
    private var isSucceed=false

    init {
        shadowPain.setColor(Color.parseColor("#66000000"))

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shadowSize=height/4f
        shadowLeft=Screen.randomInt(width/2,(width-shadowSize-1).toInt())
        topY = Screen.randomInt(shadowSize.toInt(),(height-shadowSize-1).toInt())
        println("随机区间--->${width}---->${height}--->${shadowLeft}--->${topY}--->${shadowSize}")
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        //阴影
        if(!isSucceed){
            shadowPath.moveTo(shadowLeft.toFloat(),topY.toFloat())
            shadowPath.lineTo(shadowLeft+(shadowSize/2)-(shadowSmallSize/2),topY.toFloat())

            shadowPath.lineTo(shadowLeft+(shadowSize/2)-(shadowSmallSize/2),topY+shadowSmallSize)
            shadowPath.lineTo(shadowLeft+(shadowSize/2)+(shadowSmallSize/2),topY+shadowSmallSize)
            shadowPath.lineTo(shadowLeft+(shadowSize/2)+(shadowSmallSize/2),topY.toFloat())
            shadowPath.lineTo(shadowLeft+shadowSize,topY.toFloat())

            shadowPath.lineTo((shadowSize+shadowLeft),topY+shadowSize)
            shadowPath.lineTo(shadowLeft.toFloat(),topY+shadowSize)
            shadowPath.lineTo(shadowLeft.toFloat(),topY+(shadowSize/2)+(shadowSmallSize/2))
            shadowPath.lineTo(shadowLeft+shadowSmallSize,topY+(shadowSize/2)+(shadowSmallSize/2))
            shadowPath.lineTo(shadowLeft+shadowSmallSize,topY+(shadowSize/2)-(shadowSmallSize/2))
            shadowPath.lineTo(shadowLeft.toFloat(),topY+(shadowSize/2)-(shadowSmallSize/2))
            //   shadowPath.offset((shadowInfo!!.left).toFloat(), (shadowInfo!!.top).toFloat())
            canvas.drawPath(shadowPath,shadowPain)

            //滑块
            val verfityBitmap=createBlockBitmap(shadowPath)
            canvas.drawBitmap(verfityBitmap!!, blockLeft,topY.toFloat(), bitmapPaint)
        }

    }


    /**
     * 生成拼图缺块的Bitmap
     */
    private fun createBlockBitmap(blockShape:Path): Bitmap?{
        //将原图裁剪出缺块并画出来
        val tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(tempBitmap)
        drawable.setBounds(0, 0, width, height)
        canvas.clipPath(blockShape)
        drawable.draw(canvas)

       //设置画笔
        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 10f)
        val path = Path(blockShape)
        canvas.drawPath(path, paint)
        return cropBitmap(tempBitmap)
    }


    /**
     * 保留拼图缺块大小的bitmap
     */
    private fun cropBitmap(bmp: Bitmap): Bitmap? {
        var result: Bitmap? = null
        result = Bitmap.createBitmap(bmp, shadowLeft, topY, shadowSize.toInt(), shadowSize.toInt())
        bmp.recycle()
        return result
    }


    fun setLeftPadding(distance:Float){
        blockLeft=distance
        invalidate()
    }

    fun setStop(){
        if((blockLeft/shadowLeft)>=0.975&&(blockLeft/shadowLeft)<=1.025){
            isSucceed=true
            invalidate()
            Toast.makeText(context,"验证成功",Toast.LENGTH_SHORT).show()
        }else{
            isSucceed=false
            blockLeft=0f
            invalidate()
            Toast.makeText(context,"验证失败",Toast.LENGTH_SHORT).show()
        }
    }

}