package com.example.baidumusic2.views

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.baidumusic2.R
import com.example.baidumusic2.bean.VideoBean
import com.example.baidumusic2.tools.ConvertTools
import com.example.baidumusic2.tools.MyToast
import com.example.baidumusic2.tools.NetworkUtil
import com.example.baidumusic2.tools.Screen
import kotlinx.android.synthetic.main.item_video.view.*
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*


class MyVideoLayout(var mContext: Context, attrs: AttributeSet?) : FrameLayout(mContext, attrs)
                    ,View.OnClickListener ,MyVideoView.OnVideoViewListener,SeekBar.OnSeekBarChangeListener,
                     MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener{

     constructor(context: Context):this(context,null){}


    var isFullScreen = false
   // val SCREEN_FULLSCREEN =false
    var CONTAINER_LIST = LinkedList<ViewGroup>()


    private var  entity:VideoBean?=null
    private var  position:Int=0
    private var  mTimer:Timer?=null
    private var  tvTitle:TextView
    private var  tvHint:TextView
    private var  tv_current_time:TextView
    private var  tv_total_time:TextView
    private var  seekbar:SeekBar

    private var  iv_preview:ImageView
    private var  iv_mv_again_play:ImageView
    private var  iv_mv_pause:ImageView
    private var  mv_pb:ProgressBar
    private var  vv:MyVideoView
    private var  ll_video_control:View


    init {
        val view = View.inflate(mContext, R.layout.item_video, this)

         val ivFullScreen = view.findViewById<ImageView>(R.id.mv_fullscreen)
         tvTitle=view.findViewById<TextView>(R.id.mv_title)
         tvHint=view.findViewById<TextView>(R.id.mv_hint)
         vv=view.findViewById<MyVideoView>(R.id.vv)
         iv_preview=view.findViewById<ImageView>(R.id.iv_preview)
         iv_mv_again_play=view.findViewById<ImageView>(R.id.iv_mv_play)
         iv_mv_pause=view.findViewById<ImageView>(R.id.iv_mv_pause)
         mv_pb=view.findViewById<ProgressBar>(R.id.mv_pb)
         ll_video_control=view.findViewById<View>(R.id.ll_video_control)

         tv_current_time=view.findViewById<TextView>(R.id.mv_current_time)
         tv_total_time=view.findViewById<TextView>(R.id.mv_total_time)
         seekbar=view.findViewById<SeekBar>(R.id.mv_seekbar)


         seekbar.setOnSeekBarChangeListener(this)
         ivFullScreen.setOnClickListener(this)
         iv_mv_play.setOnClickListener(this)
         iv_mv_pause.setOnClickListener(this)
         vv.setListner(this)
    }


    fun setDats(entity: VideoBean,position: Int){
        this.entity=entity
        this.position=position


        setTitle(entity.id.toString())
        setPreview(entity.thurmb)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.mv_fullscreen->{
                if (isFullScreen) {
                    //gotoScreenNormal()
                } else {
                    //gotoScreenFullscreen()
                }
            }
            R.id.iv_mv_pause->{
                if(entity?.status==4){
                    pausePlay(position)
                } else if(entity?.status==0||entity?.status==2||entity?.status==1){
                    startPlay(position)
                }
            }
            R.id.iv_mv_play->{
                startPlay(position)
            }
        }
    }


    fun gotoScreenFullscreen(){
        var vg = parent as ViewGroup
        vg.removeView(this)
        cloneAJzvd(vg)
        CONTAINER_LIST.add(vg)
        vg = Screen.scanForActivity(getContext())!!.window.decorView as ViewGroup//和他也没有关系
        vg.addView(this, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        isFullScreen = true
        Screen.hideStatusBar(getContext())
        Screen.setRequestedOrientation(mContext, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
        Screen.hideSystemUI(getContext())//华为手机和有虚拟键的手机全屏时可隐藏虚拟键 issue:1326


    }

    fun gotoScreenNormal(){
        if(CONTAINER_LIST.size!=0){
            val vg =Screen.scanForActivity(context)!!.window.decorView as ViewGroup
            vg.removeView(this)
            CONTAINER_LIST.last.removeAllViews()
            CONTAINER_LIST.last.addView(this, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            CONTAINER_LIST.pop()

            isFullScreen = false
            Screen.showStatusBar(context)
            Screen.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            Screen.showSystemUI(context)
        }

    }

    fun cloneAJzvd(vg: ViewGroup) {
        try {
            val constructor = this@MyVideoLayout.javaClass.getConstructor(Context::class.java) as Constructor<MyVideoLayout>
            val jzvd = constructor.newInstance(context)
            jzvd.setId(id)
            vg.addView(jzvd)
            vv.start()
           // jzvd.setUp(jzDataSource.cloneMe(), SCREEN_NORMAL, mediaInterfaceClass)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            println("克隆异常1--->${e.toString()}")
        } catch (e: InstantiationException) {
            e.printStackTrace()
            println("克隆异常2--->${e.toString()}")
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            println("克隆异常3--->${e.toString()}")
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            println("克隆异常4--->${e.toString()}")
        }

    }



    /**
     * 标题
     */
    fun setTitle(content:String){
        tvTitle.text="陈奕迅：有相信的人 就不是孤身一人--->${content}"
    }


    /**
     * 缩略图
     */
    fun setPreview(url:String){
//        val sceenWidth=Screen.getWidthPixel()
//        val reqHeight=Screen.dip2px(219f)
//        GlobalScope.launch {
//            val thumbnail = ConvertTools.createVideoThumbnail(url, sceenWidth, reqHeight)
//            withContext(Dispatchers.Main){
//                iv_preview.setImageBitmap(thumbnail)
//            }
//        }
        Glide.with(mContext).load(url).into(iv_preview)
    }

    /**
     * 播放音乐
     */
    private var Tag:Int=-1
    fun startPlay(tag: Int){
        Tag=tag
            entity?.let {
                val type = NetworkUtil.getNetworkType(mContext)
                if (type == 0) {
                    MyToast.short("当前网络不可用,请检查网络设置")
                    return
                }
                println("on--->Start--->${position}")
                MyToast.short(if (type == 1) "当前为wifi网络" else "当前为移动网络,请注意流量")
                vv.setVideoPath(it.path)
                vv.seekTo(it.currentduration)
                vv.start()
                vv.setOnErrorListener(this)
                vv.setOnCompletionListener(this)
                vv.setOnPreparedListener(this)
                mv_pb.visibility=View.VISIBLE
                tvHint.visibility=View.GONE
                iv_mv_pause.visibility=View.GONE
                iv_mv_again_play.visibility=View.GONE
                ll_video_control.visibility= View.GONE

                playingListener?.playing(position)
            }
    }


    /**
     * 暂停
     */
    fun pausePlay(tag:Int){
        println("on--->Pause--->${position}---->${tag}")
        vv.pause()
        vv.setOnErrorListener(null)
        vv.setOnCompletionListener(null)
        vv.setOnPreparedListener(null)
        cancelMyTimer()
        entity?.status=1
        entity?.currentduration=vv.currentPosition
        mv_pb.visibility=View.GONE
        iv_preview.visibility=View.VISIBLE
        iv_mv_pause.visibility=View.VISIBLE
        iv_mv_pause.setImageResource(R.mipmap.play_music)
    }





    /**
     * videoView触摸监听
     */
    override fun onTouchDown() {
        if(entity?.status==4){
            ll_video_control.visibility=View.VISIBLE
            iv_mv_pause.visibility= View.VISIBLE
        }
    }

    override fun onTouchCancel() {
        if(entity?.status!=1){
            iv_mv_pause.visibility= View.GONE
        }
        ll_video_control.visibility=View.GONE
    }


    override fun onPrepared(p0: MediaPlayer?) {
        if(Tag==position){
            println("on--->Prepared---->${position}--->${Tag}")
            startMyTimer()
            entity?.status=4
            mv_pb.visibility=View.GONE
            tvHint.visibility= View.GONE
            iv_preview.visibility=View.GONE
            iv_mv_pause.setImageResource(R.mipmap.iv_mv_pause)
            tv_total_time.text=ConvertTools.timeParse(vv.duration)
        }

    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
       cancelMyTimer()
       entity?.status=2
       entity?.currentduration=vv.duration
       mv_pb.visibility=View.GONE
       tvHint.visibility= View.VISIBLE
       tvHint.text="播放异常"

       iv_mv_again_play.visibility=View.VISIBLE
       return true
    }

    override fun onCompletion(p0: MediaPlayer?) {
        cancelMyTimer()
        entity?.status=0
        entity?.currentduration=0
        tvHint.visibility= View.VISIBLE
        tvHint.text="重播"
        iv_mv_again_play.visibility=View.VISIBLE
        ll_video_control.visibility=View.GONE
        iv_mv_pause.visibility= View.GONE
    }



    /**
     * SeekBar进条条
     */
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        if(entity?.status==4||entity?.status==1){
            vv.removeRunnable()
            cancelMyTimer()
        }
    }

    override fun onStopTrackingTouch(p0: SeekBar) {
        val value=(p0.progress/100.0)
        val duration=(value*(vv.duration)).toInt()
        entity?.currentduration=duration
        startPlay(position)
    }


    /**
     * 开启进度条定时器
     */
    private fun startMyTimer(){
        mTimer=Timer()
        mTimer?.schedule(object :TimerTask(){
            override fun run() {
                println("定时器运行-->${position}")
                if(vv.isPlaying){
                    post(object :Runnable{
                        override fun run() {
                            tv_current_time.text=ConvertTools.timeParse(vv.currentPosition)
                            seekbar.progress=ConvertTools.timeToProgress(vv.currentPosition,vv.duration)
                        }
                    })
                }
            }
        },0,1000)
    }

    private fun cancelMyTimer(){
        println("定时器停止-->${position}")
        mTimer?.cancel()
        mTimer=null
    }


    var playingListener:PlayingListener?=null

    interface PlayingListener{
        fun playing(position:Int)
    }

    fun onDestory(tag:Int){
        if(tag==position){
            vv.pause()
            vv.setOnErrorListener(null)
            vv.setOnCompletionListener(null)
            vv.setOnPreparedListener(null)
            vv.stopPlayback()
            cancelMyTimer()
        }
    }
}