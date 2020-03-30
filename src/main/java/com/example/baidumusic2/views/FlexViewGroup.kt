package com.example.baidumusic2.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.baidumusic2.R
import com.example.baidumusic2.tools.Screen
import java.lang.reflect.Type


class FlexViewGroup(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {


     var listener:OnFlexClickListener?=null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthModel=MeasureSpec.getMode(widthMeasureSpec)
        val widthSize=MeasureSpec.getSize(widthMeasureSpec)
        val heightModel=MeasureSpec.getMode(heightMeasureSpec)
        val heightSize=MeasureSpec.getSize(heightMeasureSpec)

        var lineWidth=0;
        var totalHeight=0
        for (index in 0..childCount-1){
             val child=getChildAt(index)
             val lp = child.layoutParams as ViewGroup.MarginLayoutParams
             measureChild(child,widthMeasureSpec,heightMeasureSpec)
             lineWidth+=child.measuredWidth+lp.rightMargin+lp.leftMargin
             if(lineWidth>widthSize){
                 //换行
                 lineWidth=child.measuredWidth+lp.rightMargin+lp.leftMargin
                 totalHeight+=child.measuredHeight+lp.topMargin+lp.bottomMargin
             }else{
                 //不换行
                totalHeight=Math.max(child.measuredHeight+lp.topMargin+lp.topMargin,totalHeight)
             }
        }

        when(heightModel){
            MeasureSpec.EXACTLY->{
              //  println("固定宽高--->${widthSize}--->${totalHeight}")
                setMeasuredDimension(widthSize,heightSize)
            }

            MeasureSpec.UNSPECIFIED->{
              //  println("不固定宽高--->${widthSize}--->${totalHeight}")
                setMeasuredDimension(widthSize,totalHeight)
            }
            MeasureSpec.AT_MOST->{
              //  println("最大值--->${widthSize}--->${totalHeight}")
                setMeasuredDimension(widthSize,totalHeight)
            }
        }


    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
         var ll=0;
         var tt=0;
         var rr=0;
         var bb=0;
         var lineWidth=0;
         for (index in  0 until childCount){
              val child=getChildAt(index)
              val lp=child.layoutParams as MarginLayoutParams
              lineWidth+=child.measuredWidth+lp.rightMargin+lp.leftMargin
              if(lineWidth>measuredWidth){
                  //换行
                  lineWidth=child.measuredWidth+lp.rightMargin+lp.leftMargin
                  ll=0+lp.leftMargin;
                  tt+=child.measuredHeight+lp.topMargin+lp.bottomMargin
                  rr=ll+child.measuredWidth
                  bb=tt+child.measuredHeight
              }else{
                  ll+=lp.leftMargin
                  tt=Math.max(lp.topMargin,tt)
                  rr=ll+child.measuredWidth
                  bb=tt+child.measuredHeight
              }
           //   println("坐标点--->${ll}--->${tt}--->${rr}--->${bb}")
              child.layout(ll,tt,rr,bb)
              ll+=child.measuredWidth+lp.rightMargin

          }
    }


    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }



    fun setDatas(){
        val typeMap= mutableMapOf<Int,String>()
        val typeRes= mutableListOf<Int>(R.mipmap.iv_newsong,R.mipmap.iv_hotsong,R.mipmap.iv_rock,
                R.mipmap.iv_jazz,R.mipmap.iv_fashion,R.mipmap.iv_newsong,R.mipmap.iv_hotsong,R.mipmap.iv_rock,
                R.mipmap.iv_jazz,R.mipmap.iv_fashion)
        typeMap.put(1,"新歌")
        typeMap.put(2,"热歌")
        typeMap.put(11,"摇滚")
        typeMap.put(12,"爵士")
        typeMap.put(16,"流行")
        typeMap.put(21,"欧美金曲")
        typeMap.put(22,"经典老歌")
        typeMap.put(23,"情歌对唱")
        typeMap.put(24,"影视金曲")
        typeMap.put(25,"网络歌曲")

        val lp=MarginLayoutParams(((Screen.getWidthPixel()-100)/5),LayoutParams.WRAP_CONTENT)
        lp.rightMargin=10
        lp.leftMargin=10
        lp.topMargin=10
        lp.bottomMargin=10
        var count=-1;
        for ((i1,i2) in typeMap){
            count+=1
            val childLayout= LinearLayout(context)
            childLayout.layoutParams=lp
            childLayout.orientation= LinearLayout.VERTICAL
            childLayout.setPadding(0,10,0,10)
            val iv= ImageView(context)
            val tv= TextView(context)
            iv.setImageResource(typeRes[count])
            tv.text=i2
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,13f)
            tv.setTextColor(Color.parseColor("#000000"))
            tv.gravity=Gravity.CENTER_HORIZONTAL
            tv.setPadding(0,10,0,0)
            childLayout.addView(iv)
            childLayout.addView(tv)

            //点击
            childLayout.setOnClickListener {
                listener?.onClick(i1,i2)
            }

            //添加
            addView(childLayout)
        }
    }

    interface OnFlexClickListener{
        fun onClick(key:Int,vaule:String)
    }
}