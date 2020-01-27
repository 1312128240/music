package com.example.baidumusic2.uis

import android.app.Service
import android.content.*
import android.os.IBinder
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.baidumusic2.MainActivity
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.R
import com.example.baidumusic2.animation.DiscTranAnimation
import com.example.baidumusic2.animation.NeedleRotateAnimation
import com.example.baidumusic2.animation.ViewRotateAnimation
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.bean.Song
import com.example.baidumusic2.broadcast.MyKeyDownReceiver
import com.example.baidumusic2.broadcast.NotifyBroadcastReceiver
import com.example.baidumusic2.broadcast.PlayBroadcastReceiver
import com.example.baidumusic2.databinding.ActivityPlayBinding
import com.example.baidumusic2.room.MusicEntity
import com.example.baidumusic2.service.PlayService
import com.example.baidumusic2.tools.*
import com.example.baidumusic2.views.PoPWindowMore
import com.example.baidumusic2.vm.PlayVm
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.layout_title_toolbar.*
import java.util.*
import kotlin.collections.ArrayList

class PlayActivity : MyBaseActivity<ActivityPlayBinding>(), SeekBar.OnSeekBarChangeListener{

    private var musicList=ArrayList<Song>()

    private var position=-1

    private var isPause:Boolean=false

    private var interval:Timer?=null

    private val playVm by lazy { ViewModelProviders.of(this).get(PlayVm::class.java) }

    private val myServiceConnection by lazy { MyServiceConnection() }

    private val playReceiver by lazy { PlayBroadcastReceiver(this) }

    private val notifyReceiver by lazy { NotifyBroadcastReceiver(this) }

    private val keyDownReceiver by lazy { MyKeyDownReceiver() }

    private val discRotateAnimation by lazy { ViewRotateAnimation(albumlayout) }

    private val needleRotateAnimation by lazy { NeedleRotateAnimation(this,iv_music_needle) }

    private val discTranAnimation by  lazy { DiscTranAnimation(this,albumlayout) }

    private var myBinder:PlayService.MyBinder?=null

    private var pw:PoPWindowMore?=null


    override fun getLayoutId(): Int {
        Screen.setStatusBar(this,true,true,R.color.colorRed)
        return R.layout.activity_play
    }

    override fun getParameter() {
        super.getParameter()
       // val musicList=intent.getSerializableExtra("musicList") as ArrayList<Song>
        musicList=intent.getParcelableArrayListExtra<Song>("musicList")
        position=intent.getIntExtra("position",-1)
    }

    override fun business() {
        initView()
        startMyService()
        registerMyReceiver()
        getMusicResources()
    }


    override fun onResume() {
        super.onResume()
        startInterval()
    }

    override fun onPause() {
        super.onPause()
        stopInterval()
    }

    override fun onDestroy() {
        super.onDestroy()
        playVm.removeNotify()
        unregisterReceiver(playReceiver)
        unregisterReceiver(notifyReceiver)
        unbindService(myServiceConnection)
        stopService(Intent(this,PlayService::class.java))
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val newPosition=intent.getIntExtra("position",-1);
            if(newPosition!=-1&&newPosition!=position){
                discRotateAnimation.endAnimation()
                position=newPosition
                getMusicResources()
            }
        }
    }

    private fun initView(){
        dataBinding?.activity=this
        seekbar.setOnSeekBarChangeListener(this)
        tvLrcView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    /**
     * 定时器获取进度
     */
   private fun startInterval(){
        interval=Timer()
        interval?.schedule(object : TimerTask(){
            override fun run() {
               runOnUiThread({
                   myBinder?.getMediaPlayer()?.let {
                       if(it.isPlaying){
                           val currentDuration=it.currentPosition
                           val totalduration=it.duration
                           val p0=(currentDuration*100.0/totalduration).toInt()
                           tv_current_time.text=TimerConvert.timeParse(currentDuration)
                           seekbar.progress=p0
                       }
                   }
               })
            }
        },0,1000)
    }


    /**
     * 取消定时器
     */
    private fun stopInterval(){
        interval?.cancel()
        interval=null
    }


    /*
     *注册广播
     */
    private  fun registerMyReceiver(){

        //播放完成广播
        val filter=IntentFilter()
        filter.addAction(Constant.ACTION_COMPLETION)
        filter.addAction(Constant.ACTINON_PREPARED)
        registerReceiver(playReceiver,filter)

        //通知栏广播
        val notifyfiter=IntentFilter();
        notifyfiter.addAction("com.example.baidumusic2.notify.next")
        notifyfiter.addAction("com.example.baidumusic2.notify.previous")
        notifyfiter.addAction("com.example.baidumusic2.notify.pause")
        notifyfiter.addAction("com.example.baidumusic2.notify.restart")
        registerReceiver(notifyReceiver,notifyfiter)

        //keydownreceiver
        val keydownfiter=IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
       // registerReceiver(keyDownReceiver,keydownfiter)
    }


    /**
     * 混合模式启动服务
     */
    private fun startMyService(){
        val i=Intent(this,PlayService::class.java)
        startService(i)
        bindService(i,myServiceConnection,Service.BIND_AUTO_CREATE)
    }


    /**
     * 獲取音樂資源信息
     */
    private  fun getMusicResources(){
        val songId=musicList[position].song_id
        playVm.getPlayResource(songId,Constant.mehtod_play).observeForever(object :Observer<MusicEntity>{
            override fun onChanged(entity: MusicEntity?) {
                 if(entity!=null){
                     dataBinding?.musicEntity=entity
                     setVaule(entity)
                 }else{
                     MyToast.short("播放音乐失败")
                 }
            }
        })
    }


    /**
     * InitUi
     */
    private  fun setVaule(entity: MusicEntity){
        //开始播放
         myBinder?.start(entity.downloadEntity?.fileLink!!)
        //图片
        Glide.with(this).load(entity.pic_big).into(iv_album)
        //Toolbar的标题
        tv_toolbar_title.text=entity.name+"  "+entity.downloadEntity?.author
        //歌词
        tvLrcView.text=entity.lrcContent
        //收藏
        iv_music_collect.setImageResource(if (entity.isCollect) R.mipmap.collect_checked else R.mipmap.collect)
        //歌曲时间
        tv_current_time.text="00:00"
        tv_total_time.text=TimerConvert.timeParse((entity.downloadEntity?.duration!!*1000))
        //更新通知栏
        playVm.updateNotification(entity,isPause)
        //进度条
        seekbar.progress=0
        //播放模式
        when(SPTools.get(Constant.KEY_PLAY_MODE)){
            "1"->iv_music_mode.setImageResource(R.mipmap.circ)
            "2"->iv_music_mode.setImageResource(R.mipmap.single_circ)
            "3"->iv_music_mode.setImageResource(R.mipmap.random)
        }
    }


    /*
     *准备就绪了，
     */
     fun prepared(){
        //动画
        if(isPause){
            isPause=false
            needleRotateAnimation.stopAnimation()
            iv_music_play.setImageResource(R.mipmap.pause)
        }
        discRotateAnimation.startAnimation()
        //更新MainFragment列表
        playVm.notifyHomeMusickList(position)
        //更新MainActivity底部动画
        val pic=musicList[position].pic_small
        playVm.notityMainAnamition(Constant.ACTION_MAIN_ANIMATION_START,pic)
    }


    /**
     * 暂停或播放
     */
    fun onClickPause(view: View,entity: MusicEntity){
        if(isNotQuickClick()){
            if(isPause) restart(entity) else pause(entity)
        }
    }

    /**
     * 暂停
     */
    fun restart(entity: MusicEntity){
        isPause=false
        myBinder?.restart()
        startInterval()
        iv_music_play.setImageResource(R.mipmap.pause)
        discRotateAnimation.restartAnimation()
        needleRotateAnimation.stopAnimation()
        //更新首页
        playVm.notityMainAnamition(Constant.ACTION_MAIN_ANIMATION_START,entity.pic_small)
        //更新通知栏
        playVm.updateNotification(entity,isPause)
    }


    /*
     *暂停后重新开始
     */
    fun pause(entity: MusicEntity){
        isPause=true
        myBinder?.pause()
        interval?.cancel()
        iv_music_play.setImageResource(R.mipmap.start)
        discRotateAnimation.pauseAnimation()
        needleRotateAnimation.startAnimation()
        //更新首页
        playVm.notityMainAnamition(Constant.ACTION_MAIN_ANIMATION_PAUSE,entity.pic_small)
        //更新通知栏
        playVm.updateNotification(entity,isPause)
    }


    /**
     * 上一首
     */
    fun onClickPrevious(view: View,entity: MusicEntity){
        if(isNotQuickClick()){
            playNextAndPrevious(false)
            discTranAnimation.rightExitAnimation()
            //更新通知栏
            playVm.updateNotification(entity,isPause)
        }
    }


    /**
     * 點擊下一首
     */
    fun onClickNext(view: View,entity: MusicEntity){
        if(isNotQuickClick()){

            playNextAndPrevious(true)
            discTranAnimation.leftExitAnimation()
            //更新通知栏
            playVm.updateNotification(entity,isPause)
        }
    }

    /**
     * 下载
     */
     fun onClickDownload(view:View,entity: MusicEntity){
         if(isNotQuickClick()){
             playVm.download(entity.songId).observe(this,object :Observer<Int>{
                 override fun onChanged(t: Int?) {
                    when(t){
                      Constant.DOWNLOADING->MyToast.short("正在下载中...")
                      Constant.DOWNLOAD_PAUSE->MyToast.short("下载已暂停")
                      Constant.DOWNLOAD_FINISH->MyToast.short("已经下载过了")
                      Constant.DOWNLOAD_INIT->MyToast.short("已加入下载队列")
                    }
                 }
             })
         }
    }


    /**
     * 切換歌曲
     */
    fun playNextAndPrevious(isNext:Boolean){
        discRotateAnimation.endAnimation()
        when(SPTools.get(Constant.KEY_PLAY_MODE)){
            "1"->{
                if(isNext){
                    position= if (position==musicList.size-1) 0 else position+1
                }else{
                    position= if (position==0) musicList.size-1 else position-1
                }
            }
            "2"->position=position
            "3"->position= (0 until musicList.size).random()
        }
        getMusicResources()
    }

    /**
     * 选择播放模式
     */
     fun changleMode(view:View){
        if(isNotQuickClick()){
            when(SPTools.get(Constant.KEY_PLAY_MODE)){
                "1"->{
                    SPTools.save(Constant.KEY_PLAY_MODE,"2")
                    iv_music_mode.setImageResource(R.mipmap.single_circ)
                }
                "2"->{
                    SPTools.save(Constant.KEY_PLAY_MODE,"3")
                    iv_music_mode.setImageResource(R.mipmap.random)
                }
                "3"->{
                    SPTools.save(Constant.KEY_PLAY_MODE,"1")
                    iv_music_mode.setImageResource(R.mipmap.circ)
                }
            }
        }
    }

    /**
     * 收藏
     */
     fun changeCollect(view: View,entity: MusicEntity){
           playVm.onChangeCollect(entity.songId).observe(this,object :Observer<MusicEntity>{
                override fun onChanged(t: MusicEntity?) {
                    t?.let {
                        iv_music_collect.setImageResource(if (it.isCollect) R.mipmap.collect_checked else R.mipmap.collect)
                    }
                }
            })
    }

    /**
     * 查看更多弹窗
     */
    fun onClickMore(view:View,entity: MusicEntity){
        pw= PoPWindowMore (this,entity)
        pw?.let {
            lifecycle.addObserver(it)
            Screen.setBackgroundAlpha(this,0.5f)
            it.showAtLocation(RootView,Gravity.BOTTOM,0,0)
            it.setOnDismissListener {  Screen.setBackgroundAlpha(this,1f) }
            it.listener=object :PoPWindowMore.onClickListener{
                override fun clickMV() {
                    pw?.dismiss()
                    pause(entity)
                    startActivity(Intent(this@PlayActivity,MvActivity::class.java))
                }

            }
        }
    }


    /**
     * 拖動進度條
     */
    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar) {
        myBinder?.drag(p0.progress)
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }


    /**
     * ServiceConnection
     */
    inner class MyServiceConnection:ServiceConnection{
       override fun onServiceDisconnected(p0: ComponentName?) {

       }

       override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            myBinder=p1 as PlayService.MyBinder
       }

   }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(MyApp.backFlag==1){
                startActivity(Intent(this,MainActivity::class.java))
                MyApp.backFlag=-1
            }else{
                moveTaskToBack(true)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



}
