package com.example.baidumusic2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.baidumusic2.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class BannerAdapter(private var mContext: Context,private var list :ArrayList<String>,private var vp:ViewPager):PagerAdapter(){

    private var interval: Disposable?=null

    private var isStop:Boolean=false

    private var RgIndicator:RadioGroup?=null


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val iv=ImageView(mContext)
        iv.scaleType=ImageView.ScaleType.FIT_XY
        Glide.with(mContext).load(list[position]).error(R.mipmap.ic_launcher).into(iv)
        container.addView(iv)
        return  iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object`as View)
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
         return view==`object`
    }

    override fun getCount(): Int {
        return list.size
    }


    private fun startInterval(){
        interval=Flowable.interval(5000,5000,TimeUnit.MILLISECONDS)
                .takeWhile { !isStop }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object: Consumer<Long>{
                    override fun accept(t: Long?) {
                        val vpIndex=vp.currentItem
                        if(vpIndex==list.size-2){
                            vp.setCurrentItem(1,false)
                        }else{
                            vp.setCurrentItem(vpIndex+1,false)
                        }
                    }
                })
    }


    fun bindIndicator(rg:RadioGroup,gravity: Int){
        RgIndicator=rg;
        val lp=RadioGroup.LayoutParams(15,15)
        lp.leftMargin=3
        lp.rightMargin=3
        rg.gravity=gravity
        for((i1,i2) in  list.withIndex()){
             val child=RadioButton(mContext)
             child.layoutParams=lp
             child.id=i1
             child.buttonDrawable=null
             child.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.banner_indicator))
             child.visibility=if(i1==0||i1==list.size-1) View.INVISIBLE else View.VISIBLE
             rg.addView(child)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun bindViewPage(){

        vp.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
                when(p1.action){
                    MotionEvent.ACTION_DOWN->{
                        println("按下--->onStop")
                        onStop()
                    }
                    MotionEvent.ACTION_MOVE->{
                        println("移动--->onStop")
                        onStop()
                    }
                    MotionEvent.ACTION_UP->{
                        println("抬起--->onStart")
                        onStart()
                    }
                }
                return false
            }

        })

        vp.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            /**
             * position 当前所在页面
             * positionOffset 当前所在页面偏移百分比
             * positionOffsetPixels 当前所在页面偏移量
             */
            override fun onPageScrolled(i: Int, v: Float, positionOffsetPixels: Int) {

                RgIndicator?.check(vp.currentItem)

                if(positionOffsetPixels==0){
                    if (i == 0){
                        //当滑到第一张图时显示最后一张图并将postion跳至"D"位置
                        vp.setCurrentItem(list.size-2,false);
                    } else if (i == list.size-1) {
                        //当滑到最后一张图时显示第一张图并将position跳至"A"位置
                        vp.setCurrentItem(1,false);
                    }
                }
            }

            override fun onPageSelected(position: Int) {

            }

        })
    }


    fun onStart(){
        println("banner--->onStart")
        isStop=false
        startInterval()
    }

    fun onStop(){
        println("banner--->onStop")
        isStop=true
        interval?.dispose()
    }

}

