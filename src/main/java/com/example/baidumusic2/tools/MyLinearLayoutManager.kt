package com.example.baidumusic2.tools

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class MyLinearLayoutManager constructor(var mContext:Context): LinearLayoutManager(mContext) {

    private var isScrollEnabled = true

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {

        val linearSmoothScroller=object :LinearSmoothScroller(mContext) {

            //滑动一个piex的时间
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return 150f / displayMetrics?.densityDpi!!;
            }

            //滑动的距离
            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                return boxStart - viewStart
            }
        }

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}