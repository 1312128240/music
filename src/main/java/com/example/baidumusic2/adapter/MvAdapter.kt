package com.example.baidumusic2.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
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
import com.example.baidumusic2.views.MyVideoLayout
import com.example.baidumusic2.views.MyVideoView
import java.util.*

class MvAdapter constructor(var mContext:Activity,var datas:ArrayList<VideoBean>,var recyclerView: RecyclerView)
       :RecyclerView.Adapter<MvAdapter.VideoViewHolder>(),LifecycleObserver{

    private var timer1:Timer?=null

    private var playPosition:Int=0

    private var isLand:Boolean=false

    init {
       setScrollListener()
    }

    class VideoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val rootView=itemView.findViewById<ViewGroup>(R.id.item_root)
        val vv=itemView.findViewById<MyVideoView>(R.id.vv)
//       // val vvContainer=itemView.findViewById<ViewGroup>(R.id.vv_container)
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

        val layout=itemView.findViewById<MyVideoLayout>(R.id.videoLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView=LayoutInflater.from(mContext).inflate(R.layout.item_mv,parent,false)
        return VideoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
             holder.layout.setTitle("${position}")

//        val item=datas[position]
//
//        holder.rootView.layoutParams=getPortraitlp()
//
//        //名字
//        holder.tvTitle.text="陈奕迅：有相信的人 就不是孤身一人  ${position}"
//
//        //已经播放当前时间
//        holder.tvCurrentTime.text=ConvertTools.timeParse(item.currentduration)
//
//        //歌曲总时间
//        holder.tvTotalTime.text=ConvertTools.timeParse(item.totalduration)
//
//        //播放或暂停
//        holder.ivPlay.setOnClickListener { clickPauseOrRestart(item,holder,position) }
//
//        holder.ivPause.setOnClickListener { clickPauseOrRestart(item,holder,position) }
//
//        //全屏播放
//        holder.ivFullscreen.setOnClickListener { onScreenChange(holder) }
//
//        //进度条
//        holder.seekBar.setProgress(ConvertTools.timeToProgress(item.currentduration,item.totalduration))
//
//        holder.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//                 holder.vv.removeRunnable()
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar) {
//                val newStartDuration=((p0.progress/100.0)*(item.totalduration)).toInt()
//                holder.vv.seekTo(newStartDuration)
//                println("${position}--->拖动到从${newStartDuration}位置开始播放")
//                p0.postDelayed(object :Runnable{
//                    override fun run() {
//                        holder.llControl.visibility=View.INVISIBLE
//                        holder.ivPlay.visibility=View.INVISIBLE
//                    }
//                },2500)
//            }
//
//        })
//
//        //播放
//        if(playPosition==position&&item.status==0){
//            onStart(item,holder,position)
//        }
//
//        //准备播放回调
//        holder.vv.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
//            override fun onPrepared(p0: MediaPlayer) {
//                if (playPosition == position&&item.status==0) {
//                    println("${position}--->从${item.currentduration}--->OnPrepared")
//                    holder.vv.start()
//                    holder.progressBar.visibility = View.INVISIBLE
//                    holder.tvHint.visibility = View.INVISIBLE
//                    startProgressInterval(item,holder)
//                    //setTouchListener(holder,position)
//                }
//            }
//        })
//
//
//        //播放完成回调
//        holder.vv.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
//            override fun onCompletion(p0: MediaPlayer?) {
//                println("${playPosition}--->OnCompletion")
//                if(playPosition==position){
//                    onNext(item,holder)
//                }
//            }
//        });
//
//        //播放错误监听
//        holder.vv.setOnErrorListener(object : MediaPlayer.OnErrorListener {
//            override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
//                println("${playPosition}--->从--->${item.currentduration}--->OnError")
//                if(playPosition==position){
//                   onError(item,holder,position)
//                }
//                return true
//            }
//        })
//
//        //触摸监听
//        holder.vv.setListner(object :MyVideoView.OnVideoViewListener{
//            override fun onDown() {
//                if(item.status==0){
//                    holder.llControl.visibility=View.VISIBLE
//                    holder.ivPlay.visibility=View.VISIBLE
//                }
//
//            }
//
//            override fun onCancel() {
//                if (item.status==0){
//                    holder.llControl.visibility=View.INVISIBLE
//                    holder.ivPlay.visibility=View.INVISIBLE
//                }
//            }
//
//        })

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
                            datas[playPosition].status=1
                            notifyItemChanged(playPosition)
                            datas[currentPosition].status=0
                            notifyItemChanged(currentPosition)
                            playPosition=currentPosition
                        }
                    }
                }
            }
        })
    }


    private fun getPortraitlp():LinearLayout.LayoutParams{
        val screenHeight=Screen.getHeightPixel()
        val toolbarBarHeight=Screen.dip2px(68f)
        val resultHeight=(screenHeight-toolbarBarHeight)/2
        val lp=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,resultHeight)
        return lp
    }

    private fun getLandscapelp():LinearLayout.LayoutParams{
        val scrrenWidth=Screen.getWidthPixel()
        val screenHeight=Screen.getHeightPixel()
        val lp=LinearLayout.LayoutParams(scrrenWidth,screenHeight)
        return lp
    }

    /**
     * 开始播放初始化
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun onStart(item: VideoBean,holder: VideoViewHolder,position: Int){
        val type = NetworkUtil.getNetworkType(mContext)
        if (type == 0) {
            MyToast.short("当前网络不可用,请检查网络设置")
            return
        }
        MyToast.short(if (type == 1) "当前为wifi网络" else "当前为移动网络,请注意流量")
        holder.vv.setVideoPath(item.path)
        holder.vv.seekTo(item.currentduration)
        holder.vv.start()

        holder.ivPlay.setImageResource(R.mipmap.iv_pause_mv)
        holder.ivPlay.visibility = View.INVISIBLE
        holder.ivPause.visibility=View.INVISIBLE
        holder.llControl.visibility=View.INVISIBLE

        holder.progressBar.visibility = View.VISIBLE
        holder.tvHint.visibility = View.VISIBLE
        holder.tvHint.text = "加载中..."

        item.status=0
    }


    /**
     * 当暂停时
     */
    private fun onPause(item: VideoBean,holder: VideoViewHolder,position: Int){
        println("${position}--->从${item.currentduration}---->正在播放暂停")
        holder.vv.pause()
        holder.ivPlay.setImageResource(R.mipmap.iv_play_mv)
        holder.ivPlay.visibility=View.INVISIBLE
        holder.ivPause.visibility=View.VISIBLE
        holder.llControl.visibility=View.INVISIBLE
        holder.progressBar.visibility=View.INVISIBLE
        holder.tvHint.visibility=View.INVISIBLE
        item.status=1
        stopProgressInterval()
    }


    /**
     * 当错误时
     */
    private fun onError(item: VideoBean,holder: VideoViewHolder,position: Int){
        item.status=1
        holder.vv.pause()
        holder.ivPause.visibility=View.VISIBLE

        holder.ivPlay.visibility=View.INVISIBLE
        holder.progressBar.visibility=View.INVISIBLE
        holder.llControl.visibility=View.INVISIBLE

        holder.tvHint.visibility=View.VISIBLE
        holder.tvHint.text="播放异常"
        stopProgressInterval()
    }


    private fun onScreenChange(holder: VideoViewHolder){
        if(isLand){
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
            holder.rootView?.layoutParams=getPortraitlp()
            isLand=false
        }else{
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
            holder.rootView?.layoutParams=getLandscapelp()
            isLand=true
        }
        listener?.onChange(isLand,holder)
    }


     fun onActivityBackPress(holder: VideoViewHolder){
         holder.rootView?.layoutParams=getPortraitlp()
         listener?.onChange(false,holder)
         isLand=false
     }


    var listener:OnConfigChangeListener?=null

    interface OnConfigChangeListener{
         fun onChange(isLand: Boolean,holder: VideoViewHolder)
    }


    /**
     * 当播放完成下一首
     */
   private fun onNext(item: VideoBean,holder: VideoViewHolder){
        stopProgressInterval()
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
                onPause(item,holder,playPosition)
                //下一个开始播放
                playPosition+=1
                onStart(datas[playPosition],holder,playPosition)
            }else{
                //最后一个暂停
                onPause(item,holder,playPosition)
            }
        }
    }



    /**
     * 暂停或开始
     */
    private fun clickPauseOrRestart(item: VideoBean,holder: VideoViewHolder,position: Int){
        if(isNotQuickClick()){
            val type = NetworkUtil.getNetworkType(mContext)
            if (type == 0) {
                MyToast.short("当前网络不可用,请检查网络设置")
                return
            }
           if(position==playPosition){
               if(item.status==0) onPause(item,holder,position) else onStart(item,holder,position)
           }else{
               val isBottom=recyclerView.canScrollVertically(1)
               if(isBottom){
                   recyclerView.smoothScrollToPosition(playPosition+1)
               }else{
                   onPause(datas[playPosition],holder,playPosition)
                   playPosition=position
                   onStart(item,holder,position)
               }
           }
        }

    }


    /**
     * 开启进度条定时器
     */
    private fun startProgressInterval(item: VideoBean,holder: VideoViewHolder){
        timer1=Timer()
        timer1?.schedule(object :TimerTask(){
            override fun run() {
                mContext.runOnUiThread({
                    if(holder.vv.isPlaying){
                         item.currentduration=holder.vv.currentPosition
                         holder.tvCurrentTime.text=ConvertTools.timeParse(item.currentduration)
                         holder.seekBar.progress=ConvertTools.timeToProgress(item.currentduration,item.totalduration)
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


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onActivityPause(){
        stopProgressInterval()
    }

}