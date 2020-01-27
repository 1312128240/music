package com.example.baidumusic2.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baidumusic2.R
import com.example.baidumusic2.bean.VideoBean
import com.example.baidumusic2.tools.*
import java.util.*

class MvAdapter constructor(var mContext:Activity,var datas:ArrayList<VideoBean>,var recyclerView: RecyclerView)
       :RecyclerView.Adapter<MvAdapter.VideoViewHolder>(),LifecycleObserver{

    private var timer1:Timer?=null

    private var timer2:Timer?=null

    private var playPosition:Int=0

    private var isChangeScreen:Boolean=false


    init {
       setScrollListener()
    }

    class VideoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val vv=itemView.findViewById<VideoView>(R.id.vv)
        val vvContainer=itemView.findViewById<ViewGroup>(R.id.vv_container)
        val ivPlay=itemView.findViewById<ImageView>(R.id.iv_mv_play)
        val ivPause=itemView.findViewById<ImageView>(R.id.iv_mv_pause)
        val seekBar=itemView.findViewById<SeekBar>(R.id.mv_seekbar)
        val tvCurrentTime=itemView.findViewById<TextView>(R.id.mv_current_time)
        val tvTotalTime=itemView.findViewById<TextView>(R.id.mv_total_time)
        val tvTitle=itemView.findViewById<TextView>(R.id.mv_title)
        val llControl=itemView.findViewById<View>(R.id.ll_video_control)
        val progressBar=itemView.findViewById<View>(R.id.mv_pb)
        val tvHint=itemView.findViewById<TextView>(R.id.mv_hint)
        val ivFullscreen=itemView.findViewById<ImageView>(R.id.mv_fullscreen)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView=LayoutInflater.from(mContext).inflate(R.layout.item_video,parent,false)
        val vv_container=itemView.findViewById<View>(R.id.vv_container)
        vv_container.layoutParams= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,setPorTraitVideoViewHeight())
        return VideoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        val item=datas[position]

        item.beanHolder=holder

        //名字
        holder.tvTitle.text="陈奕迅：有相信的人 就不是孤身一人  ${position}"

        //已经播放当前时间
        holder.tvCurrentTime.text=TimerConvert.timeParse(item.currentduration)

        //歌曲总时间
        holder.tvTotalTime.text=TimerConvert.timeParse(item.totalduration)

        //播放或暂停
        holder.ivPlay.setOnClickListener { clickPauseOrRestart(item,position) }

        holder.ivPause.setOnClickListener { clickPauseOrRestart(item,position) }

        //全屏播放
        holder.ivFullscreen.setOnClickListener { onScreenChange() }

        //进度条
        holder.seekBar.setProgress(TimerConvert.timeToProgress(item.currentduration,item.totalduration))

        holder.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar) {
                val newStartDuration=((p0.progress/100.0)*(item.totalduration)).toInt()
                holder.vv.seekTo(newStartDuration)
                println("${position}--->拖动到从${newStartDuration}位置开始播放")
            }

        })


        //播放
        if(playPosition==position&&item.status==0){
            onStart(item,position)
        }

        //准备播放回调
        holder.vv.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(p0: MediaPlayer) {
                if (playPosition == position&&item.status==0) {
                    println("${position}--->从${item.currentduration}--->OnPrepared")
                    holder.vv.start()
                    holder.progressBar.visibility = View.INVISIBLE
                    holder.tvHint.visibility = View.INVISIBLE
                    startProgressInterval(item)
                    setTouchListener(holder,position)
                }
            }
        })


        //播放完成回调
        holder.vv.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(p0: MediaPlayer?) {
                println("${playPosition}--->OnCompletion")
                if(playPosition==position){
                    onNext(item)
                }
            }
        });


        //播放错误监听
        holder.vv.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                println("${playPosition}--->从--->${item.currentduration}--->OnError")
                if(playPosition==position){
                   onError(item,position)
                }
                return true
            }
        })

    }



    /**
     * 滑动到的位置监听
     */
    private fun setScrollListener(){
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState){
                    RecyclerView.SCROLL_STATE_IDLE ->{
                        val manager=recyclerView.layoutManager as LinearLayoutManager
                        val currentPosition = manager.findFirstVisibleItemPosition();
                        //如果滑动停止后还是当前的正在播放则不执行
                        if(playPosition!=currentPosition){
                           onPause(datas[playPosition],playPosition)
                           playPosition=currentPosition
                           onStart(datas[currentPosition],currentPosition)
                        }
                    }
                }
            }
        })
    }



    /**
     * 竖屏时一个videoview的高度
     */
    private fun setPorTraitVideoViewHeight():Int{
        val screenHeight=Screen.getHeightPixel()
        val toolbarBarHeight=Screen.dip2px(68f)
        val itemOtherHeight=Screen.dip2px(220f)
       // val statusBarHeigth=Screen.getStatusBarHeight()
        return (screenHeight-toolbarBarHeight-itemOtherHeight)/2
    }


    /**
     * 当横屏时设置VideoView的高度
     */
    private fun setLandscapeVideoViewHeight():Int{
        val screenHeight=Screen.getHeightPixel()
        return screenHeight
    }



    /**
     * 开始播放初始化
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun onStart(item: VideoBean,position: Int){
        val type = NetworkUtil.getNetworkType(mContext)
        if (type == 0) {
            MyToast.short("当前网络不可用,请检查网络设置")
            return
        }
        MyToast.short(if (type == 1) "当前为wifi网络" else "当前为移动网络,请注意流量")
        item.beanHolder?.let {
            it.vv.setVideoPath(item.path)
            it.vv.seekTo(item.currentduration)
            it.vv.start()

            it.ivPlay.setImageResource(R.mipmap.iv_pause_mv)
            it.ivPlay.visibility = View.INVISIBLE
            it.ivPause.visibility=View.INVISIBLE
            it.llControl.visibility=View.INVISIBLE

            it.progressBar.visibility = View.VISIBLE
            it.tvHint.visibility = View.VISIBLE
            it.tvHint.text = "加载中..."

            item.status=0
            removeTouchListener(it,position)
        }
    }


    /**
     * 当暂停时
     */
    private fun onPause(item: VideoBean,position: Int){
        item.beanHolder?.let {
            println("${position}--->从${item.currentduration}---->正在播放暂停")
            it.vv.pause()
            it.ivPlay.setImageResource(R.mipmap.iv_play_mv)
            it.ivPlay.visibility=View.INVISIBLE
            it.ivPause.visibility=View.VISIBLE
            it.llControl.visibility=View.INVISIBLE
            it.progressBar.visibility=View.INVISIBLE
            it.tvHint.visibility=View.INVISIBLE
            item.status=1
            stopProgressInterval()
            removeTouchListener(it,position)
        }
    }




    /**
     * 当错误时
     */
    private fun onError(item: VideoBean,position: Int){
        item.beanHolder?.let {
            item.status=1
            it.vv.pause()
            it.ivPause.visibility=View.VISIBLE

            it.ivPlay.visibility=View.INVISIBLE
            it.progressBar.visibility=View.INVISIBLE
            it.llControl.visibility=View.INVISIBLE

            it.tvHint.visibility=View.VISIBLE
            it.tvHint.text="播放异常"
            removeTouchListener(it,position)
            stopProgressInterval()
            stopControlInterval()
        }

    }


    private fun onScreenChange(){
        if(isChangeScreen){
            //竖屏时
            isChangeScreen=false
            //   Screen.setChangeScreen(mContext,false)
            //   holder.vvContainer.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,setPorTraitVideoViewHeight())

        }else{
            //横屏时
            isChangeScreen=true
            //  Screen.setChangeScreen(mContext,true)
            //  holder.vvContainer.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,setLandscapeVideoViewHeight())
        }
    }


    /**
     *设置触摸监听
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(holder: VideoViewHolder,position: Int){
        println("设置触摸监听--${position}")

        holder.vv.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when(p1?.action){
                    MotionEvent.ACTION_DOWN->{
                        controlVisible(holder)
                    }
                    MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                        controlInvisible(holder)
                    }
                }
                return true
            }

        })

        holder.seekBar.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when(p1?.action){
                    MotionEvent.ACTION_DOWN->{
                        controlVisible(holder)
                    }
                    MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                        controlInvisible(holder)
                    }
                }
                return false
            }

        })

    }


    /**
     *删除触摸监听
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun removeTouchListener(holder: VideoViewHolder,position: Int){
        println("取消触摸监听--${position}")
        holder.vv.setOnTouchListener(null)
        holder.seekBar.setOnTouchListener(null)
    }


    /**
     * 当播放完成下一首
     */
   private fun onNext(item: VideoBean){
        stopProgressInterval()
        stopControlInterval()
        item.currentduration=0

        val type = NetworkUtil.getNetworkType(mContext)
        if (type == 0) {
            MyToast.short("当前网络不可用,请检查网络设置")
            return
        }
        val isBottom=recyclerView.canScrollVertically(1)
        if(isBottom){
            recyclerView.smoothScrollToPosition(playPosition+1)
        }else{
            //到底部了
            if(playPosition+1<datas.size){
                //上一个暂停
                onPause(item,playPosition)
                //下一个开始播放
                playPosition+=1
                onStart(datas[playPosition],playPosition)
            }else{
                //最后一个暂停
                onPause(item,playPosition)
            }
        }
    }



    /**
     * 暂停或开始
     */
    private fun clickPauseOrRestart(item: VideoBean,position: Int){
        if(isNotQuickClick()){
            val type = NetworkUtil.getNetworkType(mContext)
            if (type == 0) {
                MyToast.short("当前网络不可用,请检查网络设置")
                return
            }
           if(position==playPosition){
               if(item.status==0) onPause(item,position) else onStart(item,position)
           }else{
               val isBottom=recyclerView.canScrollVertically(1)
               if(isBottom){
                   recyclerView.smoothScrollToPosition(playPosition+1)
               }else{
                   onPause(datas[playPosition],playPosition)
                   playPosition=position
                   onStart(item,position)
               }
           }
        }

    }



    /**
     * 隐藏控制栏
     */
    private fun controlInvisible(holder: VideoViewHolder){
        timer2=Timer()
        timer2?.schedule(object :TimerTask(){
            override fun run() {
                mContext.runOnUiThread({
                    stopControlInterval()
                    holder.llControl.visibility=View.INVISIBLE
                    holder.ivPlay.visibility=View.INVISIBLE
                })
            }
        },2000)
    }



    /**
     * 显示控件栏
     */
    private fun controlVisible(holder: VideoViewHolder){
        stopControlInterval()
        holder.llControl.visibility=View.VISIBLE
        holder.ivPlay.visibility=View.VISIBLE
    }



    /**
     * 开启进度条定时器
     */
    private fun startProgressInterval(item: VideoBean){
        timer1=Timer()
        timer1?.schedule(object :TimerTask(){
            override fun run() {
                mContext.runOnUiThread({
                    item.beanHolder?.let {
                        if(it.vv.isPlaying){
                            item.currentduration=it.vv.currentPosition
                            it.tvCurrentTime.text=TimerConvert.timeParse(item.currentduration)
                            it.seekBar.progress=TimerConvert.timeToProgress(item.currentduration,item.totalduration)
                            // println("从当前进度--->${item.currentduration}")
                        }
                    }

                })
            }

        },0,1000)
    }



    /**
     * 关闭进度条定时器
     */
    private fun stopProgressInterval(){
        timer1?.cancel()
        timer1=null
    }


    /**
     * 关闭控制栏
     */
    private fun stopControlInterval(){
        timer2?.cancel()
        timer2=null
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onActivityPause(){
        stopControlInterval()
        stopProgressInterval()
    }

}