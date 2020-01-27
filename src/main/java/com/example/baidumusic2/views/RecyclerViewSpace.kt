package com.example.baidumusic2.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSpace(mContext:Context, color:Int,var divHeight:Int):RecyclerView.ItemDecoration() {

    //头部是否添加偏移量
    private var headerOffsets:Boolean=false

    constructor(mContext:Context, color:Int,divHeight:Int,headerOff:Boolean):this(mContext,color,divHeight){
         this.headerOffsets=headerOff
    }

    private var paint=Paint()

    init {
        paint.color=mContext.resources.getColor(color)
        paint.strokeWidth=divHeight.toFloat()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        for(i in 0 until parent.childCount){
            if(headerOffsets){
                outRect.set(0,0,0,divHeight)
            }else{
                if(i!=0){
                    outRect.set(0,0,0,divHeight)
                }
            }
        }
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount){
            val itemView=parent.getChildAt(i)
            val l=itemView.left.toFloat()
            val t=itemView.top.toFloat()
            val r=itemView.right.toFloat()
            val b=itemView.bottom.toFloat()

            if(i!=parent.childCount-1){
                c.drawRect(l,b,r,divHeight+b,paint)
            }
        }

    }
}