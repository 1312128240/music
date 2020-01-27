package com.example.baidumusic2.vm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.R
import com.example.baidumusic2.Repository.PlayRepository
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.bean.PlayBean
import com.example.baidumusic2.download.DownloadManage
import com.example.baidumusic2.download.DownloadTask
import com.example.baidumusic2.retrofit.RetrofitUtil
import com.example.baidumusic2.room.AppDatabase
import com.example.baidumusic2.room.DownloadEntity
import com.example.baidumusic2.room.MusicEntity
import com.example.baidumusic2.tools.MyPrint
import com.example.baidumusic2.tools.StringTools
import com.example.baidumusic2.uis.PlayActivity
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext


class PlayVm :ViewModel(),CoroutineScope{

    private var job=Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+job


    private val playRepository by lazy { PlayRepository() }


    /**
     * 獲取音樂信息
     */
    fun getPlayResource(songId:String,method:String):LiveData<MusicEntity>{
        val mutableLiveData=MutableLiveData<MusicEntity>()
          launch {
                      var musicEntity=AppDatabase.getAppDatabase().getMusicDao().queryOne(songId)
                       if(musicEntity!=null){
                            val downloadEntity=musicEntity.downloadEntity
                            if(downloadEntity?.status==Constant.DOWNLOAD_FINISH){
                              val file=File(Constant.downloadMusicPath+"${downloadEntity.title}.${downloadEntity.fileExtension}")
                              if(file.exists()){
                                  MyPrint.pln("${downloadEntity.title}-->歌曲从文件加载")
                                  downloadEntity.fileLink=file.toString()
                              }else{
                                  MyPrint.pln("${downloadEntity.title}-->歌曲文件夹不存在,从网络加载")
                                  val playBean= RetrofitUtil.getServiceApi().getPlaySource(songId,method)
                                  musicEntity=updateMusicEntity(musicEntity.songId,playBean)
                              }
                          }else{
                               MyPrint.pln("${downloadEntity?.title}-->歌曲未下载,,从网络加载")
                               val playBean= RetrofitUtil.getServiceApi().getPlaySource(songId,method)
                               musicEntity=updateMusicEntity(musicEntity.songId,playBean)
                          }
                       }else{
                           MyPrint.pln("歌曲第一次添加进数据库")
                           val playBean= RetrofitUtil.getServiceApi().getPlaySource(songId,method)
                           musicEntity=insertMusicEntity(playBean)
                       }
              withContext(Dispatchers.Main){
                  mutableLiveData.value=musicEntity
              }
          }

        return mutableLiveData
    }

    /**
     * 将playBean转化成DownloadEntity,并保存进数据库
     */
    private fun insertMusicEntity(t:PlayBean):MusicEntity?{
        var entity:MusicEntity?=null
        val songinfo=t.songinfo
        val bitrate=t.bitrate
        if(songinfo!=null&&bitrate!=null) {
            //下载歌词
            val lrcContent=StringTools.downloadText(songinfo.lrclink)
            //保存进数据库
            val downloadEntity = DownloadEntity(songinfo.song_id, songinfo.title, bitrate.file_link, songinfo.author,
                    bitrate.file_extension,bitrate.file_size, bitrate.file_duration, Constant.DOWNLOAD_INIT, 0, 0)
            entity = MusicEntity(songinfo.song_id, songinfo.title, false, songinfo.lrclink,lrcContent, songinfo.pic_big, songinfo.pic_small, downloadEntity)
            AppDatabase.getAppDatabase().getMusicDao().insertAll(entity)
        }
        return entity
    }

    /**
     *
     */
    private fun updateMusicEntity(songId: String,t:PlayBean?):MusicEntity?{
        val entity= AppDatabase.getAppDatabase().getMusicDao().queryOne(songId)
        if(entity!=null&&t?.bitrate!=null){
            entity.downloadEntity?.fileLink=t.bitrate.file_link
            AppDatabase.getAppDatabase().getMusicDao().updateAll(entity)
        }
        return entity
    }


    /**
     * 刷新MainFragment音乐列表
     */
    fun notifyHomeMusickList(position:Int){
       val intent=Intent("com.example.baidumusic2.broadcast.HomeBroadcastReceiver")
        intent.action=Constant.ACTINON_NOTIFY_HOME
        intent.putExtra("click_position",position)
        MyApp.getContext().sendBroadcast(intent)
    }


    /**
     * 刷新首页底部旋转动画
     */
    fun notityMainAnamition(action:String,pic:String){
        val intent=Intent("com.example.baidumusic2.broadcast.MainBroadcastReceiver")
        intent.action=action
        intent.putExtra("pic",pic)
        MyApp.getContext().sendBroadcast(intent)
    }



    /**
     * 切换收藏
     */
     fun onChangeCollect(songId: String):LiveData<MusicEntity>{
        val liveData=MutableLiveData<MusicEntity>()
        launch {
            val queryBean=AppDatabase.getAppDatabase().getMusicDao().queryOne(songId)
            if(queryBean!=null){
                queryBean.isCollect=!queryBean.isCollect
                AppDatabase.getAppDatabase().getMusicDao().updateAll(queryBean)
            }
            withContext(Dispatchers.Main){
                 liveData.value=queryBean
            }
        }
        return liveData
    }


    /**
     * 下载bitmap开启通知栏
     */
    fun updateNotification(musicEntity: MusicEntity?,isPause:Boolean){
        musicEntity?.let {
            launch {
                Glide.with(MyApp.getContext()).asBitmap().load(it.pic_small).into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                       // sendCustomViewNotification(it,resource,isPause)
                    }
                });
            }
        }
    }

    /**
     * 下载
     */
    fun download(songId: String):LiveData<Int>{
        val downloadLiveData=MutableLiveData<Int>()
        launch {
            val entity=AppDatabase.getAppDatabase().getMusicDao().queryOne(songId)
            entity?.downloadEntity?.let {
                if(it.status==Constant.DOWNLOAD_INIT){
                    AppDatabase.getAppDatabase().getMusicDao().updateAll(entity)
                    DownloadTask.addTask(entity.songId, DownloadManage(it))
                 }
            }
            withContext(Dispatchers.Main){
                downloadLiveData.value=entity?.downloadEntity?.status
            }
        }

        return downloadLiveData
    }


    /**
     * 自定义播放器通知栏
     */
    private  fun sendCustomViewNotification(entity: MusicEntity, bitmap: Bitmap, isPause:Boolean) {
        //普通notification用到的视图
        val remoteViews = RemoteViews(MyApp.getContext().packageName, R.layout.layout_notify_widget)
        //显示bigView的notification用到的视图
      //  val bigView = RemoteViews(MyApp.getContext().packageName, R.layout.notify_big_layout)
        //歌曲名
        remoteViews.setTextViewText(R.id.tv_notif_music_name,entity.name)
        //作者
        remoteViews.setTextViewText(R.id.tv_notif_music_author,entity.downloadEntity?.author)
        //图片
        remoteViews.setImageViewBitmap(R.id.iv_notif_music_pic,bitmap)
        //播放
        remoteViews.setImageViewResource(R.id.ib_notify_music_pause,if(isPause) R.mipmap.start else R.mipmap.pause)
        //上一首
        val intent1= Intent("com.example.baidumusic2.broadcast.NotifyBroadcastReceiver")
        intent1.action="com.example.baidumusic2.notify.previous"
        remoteViews.setOnClickPendingIntent(R.id.ib_notify_music_previous,
                PendingIntent.getBroadcast(MyApp.getContext(),101,intent1, PendingIntent.FLAG_UPDATE_CURRENT))
        //下一首
        val intent2= Intent("com.example.baidumusic2.broadcast.NotifyBroadcastReceiver")
        intent2.action="com.example.baidumusic2.notify.next"
        remoteViews.setOnClickPendingIntent(R.id.ib_notify_music_next,
                PendingIntent.getBroadcast(MyApp.getContext(),102,intent2, PendingIntent.FLAG_UPDATE_CURRENT))
        //暂停
        val intent3= Intent("com.example.baidumusic2.broadcast.NotifyBroadcastReceiver")
        if(isPause)intent3.action="com.example.baidumusic2.notify.restart" else intent3.action="com.example.baidumusic2.notify.pause"
        remoteViews.setOnClickPendingIntent(R.id.ib_notify_music_pause,
                PendingIntent.getBroadcast(MyApp.getContext(),103,intent3, PendingIntent.FLAG_UPDATE_CURRENT))
        //notifyManager
        val manager = MyApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //指定打开页面
        val appIntent = Intent(MyApp.getContext(), PlayActivity::class.java)
        val contentIntent = PendingIntent.getActivity(MyApp.getContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //Android 26要加channerl
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val id ="channel_1";//channel的id
            val importance = NotificationManager.IMPORTANCE_LOW;//channel的重要性
            val channel = NotificationChannel(id, "123", importance);//生成channel
            val notification = NotificationCompat.Builder(MyApp.getContext(),id)
                    .setOngoing(true)
                    .setTicker("")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(remoteViews)//设置普通notification视图
                    .setContentIntent(contentIntent)
                    //.setCustomBigContentView(bigView)//设置显示bigView的notification视图
                    // .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    // .setLights()
                    //.setVibrate()
                    .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                    .build()
            manager.createNotificationChannel(channel);//添加channel
            manager.notify(2, notification)
        }else{
            val notification = NotificationCompat.Builder(MyApp.getContext())
                    .setOngoing(true)
                    .setTicker("")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent)
                    .setContent(remoteViews)//设置普通notification视图
                    //.setCustomBigContentView(bigView)//设置显示bigView的notification视图
                    // .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                    .build()
            manager.notify(2, notification)
        }

    }


    /**
     * 移除通知栏消息
     */
    fun removeNotify(){
        val nMgr =MyApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancel(2)
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}