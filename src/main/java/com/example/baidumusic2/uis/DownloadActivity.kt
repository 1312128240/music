package com.example.baidumusic2.uis

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.baidumusic2.R
import com.example.baidumusic2.adapter.BaseRecyclerAdapter
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.databinding.ActivityDownloadBinding
import com.example.baidumusic2.download.DownloadManage
import com.example.baidumusic2.download.DownloadTask
import com.example.baidumusic2.room.AppDatabase
import com.example.baidumusic2.room.DownloadEntity
import com.example.baidumusic2.room.MusicEntity
import com.example.baidumusic2.tools.StringTools
import com.example.baidumusic2.tools.isNotQuickClick
import com.example.baidumusic2.views.RecyclerViewSpace
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.layout_title_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DownloadActivity : MyBaseActivity<ActivityDownloadBinding>() {

    private var downloadAdapter:BaseRecyclerAdapter<MusicEntity>?=null

    private var timer:Timer?=null

    override fun getLayoutId(): Int {
      return R.layout.activity_download
    }


    override fun business() {
        setToolbar()
        initAdapter()
    }


    override fun onResume() {
        super.onResume()
        startInterval()
    }

    override fun onPause() {
        super.onPause()
        stopInterval()
    }

    fun setToolbar(){
           toolbar.setBackgroundColor(resources.getColor(R.color.colorWhite))
           tv_toolbar_title.text="正在下载"
           iv_toolbar_back.setOnClickListener { onBackPressed() }
    }


    fun startInterval(){
        timer=Timer()
        timer?.schedule(object :TimerTask(){
            override fun run() {
                launch {
                    val list=AppDatabase.getAppDatabase().getMusicDao().queryUnfinished()
                    println("查询未完成--->${list.size}")
                    withContext(Dispatchers.Main){
                        downloadAdapter?.add(list,true)
                    }
                }
            }
        },0,1000)
    }

    fun stopInterval(){
        if(timer!=null){
            timer?.cancel()
            timer=null
        }
    }


    fun initAdapter(){
        downloadAdapter=object :BaseRecyclerAdapter<MusicEntity>(this, arrayListOf(),R.layout.item_download){
            override fun bindViewHoler(holder: ContentViewHolder, position: Int, bean: MusicEntity) {
                //名字
                holder.setText(R.id.tv_locality_name,"${bean.name}")
                //进度
                holder.setText(R.id.tv_locality_progress,"(${bean.downloadEntity?.progress}%)")
                //删除
                holder.getView<ImageView>(R.id.iv_del_download)?.setOnClickListener {
                    if(isNotQuickClick()){
                        dele(bean.downloadEntity,holder.adapterPosition)
                    }
                }
                val tvDescription=holder.getView<TextView>(R.id.tv_locality_description)
                 val pb=holder.getView<ProgressBar>(R.id.pb)
                //状态
                if(bean.downloadEntity?.status==Constant.DOWNLOADING){
                    //描述
                    tvDescription?.setTextColor(Color.parseColor("#aaaaaa"))
                    tvDescription?.text="${StringTools.toM(bean.downloadEntity?.startPoistion!!)}M/${StringTools.toM(bean.downloadEntity?.fileSize!!)}M"
                    //进度条
                    pb?.visibility=View.VISIBLE
                    pb?.setProgress(bean.downloadEntity?.progress!!)
                }else  if(bean.downloadEntity?.status==Constant.DOWNLOAD_PAUSE){
                    //描述
                    tvDescription?.setTextColor(Color.parseColor("#aaaaaa"))
                    tvDescription?.text="已暂停,点击继续下载"
                    //进度
                    pb?.visibility= View.GONE
                }else{
                    //描述
                    tvDescription?.setTextColor(mContext.resources.getColor(R.color.colorRed))
                    tvDescription?.text="下载异常,请重试"
                    //进度
                    pb?.visibility= View.GONE
                }
            }

        }
        recy_my_download.setHasFixedSize(true)
        recy_my_download.addItemDecoration(RecyclerViewSpace(this,R.color.colorGray22,2))
        recy_my_download.adapter=downloadAdapter
        downloadAdapter?.listener=object : BaseRecyclerAdapter.OnItemClickListener<MusicEntity>{
            override fun OnClick(position: Int, bean: MusicEntity, lists: ArrayList<MusicEntity>) {
                 if(isNotQuickClick()){
                     PauseAndRestatr(bean.downloadEntity,position)
                 }
            }
        }
    }

    /*
     *删除任务
     */
    private fun dele(entity: DownloadEntity?,position:Int){
        launch {
            stopInterval()
            //删除数据库
            entity?.let {
                if(it.status==Constant.DOWNLOADING){
                    val manager= DownloadTask.getTask(entity.downloadId)
                    if(manager!=null){
                        manager.downloadCancel()
                    }else{
                        AppDatabase.getAppDatabase().getMusicDao().deleteId(entity.downloadId)
                    }
                }else{
                    AppDatabase.getAppDatabase().getMusicDao().deleteId(entity.downloadId)
                }
            }

            //删除file
            val file= File(Constant.downloadMusicPath+"${entity?.title}.${entity?.fileExtension}")
            if(file.exists()){
               file.delete()
            }
            withContext(Dispatchers.Main){
                downloadAdapter?.notifyItemRemoved(position)
            }
            startInterval()
        }
    }


    /*
     *暂停或重新开始
     */
    private fun PauseAndRestatr(entity: DownloadEntity?,position:Int){
        launch {
            stopInterval()
            entity?.let {
                if(entity.status==Constant.DOWNLOADING){
                    val manager= DownloadTask.getTask(entity.downloadId)
                    if(manager!=null){
                        entity.status=Constant.DOWNLOAD_PAUSE
                        manager.downloadPause()
                    }else{
                        entity.status=Constant.DOWNLOAD_PAUSE
                       val x= AppDatabase.getAppDatabase().getMusicDao().queryOne(entity.downloadId)
                       x?.let {
                           x.downloadEntity=entity
                           AppDatabase.getAppDatabase().getMusicDao().updateAll(x)
                       }
                    }
                }else{
                    entity.status=Constant.DOWNLOADING
                    DownloadTask.addTask(entity.downloadId, DownloadManage(entity))
                }
            }

            withContext(Dispatchers.Main){
                downloadAdapter?.notifyItemChanged(position)
            }
            startInterval()
        }
    }
}
