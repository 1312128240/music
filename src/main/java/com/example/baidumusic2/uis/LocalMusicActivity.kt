package com.example.baidumusic2.uis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import android.widget.Filter
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.baidumusic2.R
import com.example.baidumusic2.adapter.BaseRecyclerAdapter
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.databinding.ActivityDownloadBinding
import com.example.baidumusic2.download.DownloadManage
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

class LocalMusicActivity : MyBaseActivity<ActivityDownloadBinding>(){

    private var downloadAdapter: BaseRecyclerAdapter<MusicEntity>?=null

    private var succeedReceiver:DownloadSucceedReceiver?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_download
    }


    override fun business() {
        setToolbar()
        initAdapter()
        initReceiver()
    }

    fun initReceiver(){
         succeedReceiver=DownloadSucceedReceiver()
         val filter= IntentFilter()
         filter.addAction(Constant.ACTION_SUCCEED)
         registerReceiver(succeedReceiver,filter)
    }



    override fun onResume() {
        super.onResume()
        queryDatas()
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(succeedReceiver)
    }

    fun setToolbar(){
        toolbar.setBackgroundColor(resources.getColor(R.color.colorWhite))
        tv_toolbar_title.text="我的歌单"
        iv_toolbar_back.setOnClickListener { onBackPressed() }
    }


    fun  queryDatas(){
        launch {
            val list= AppDatabase.getAppDatabase().getMusicDao().queryFinish()
            withContext(Dispatchers.Main){
                downloadAdapter?.add(list,true)
            }
        }
    }


    fun initAdapter(){
        downloadAdapter=object : BaseRecyclerAdapter<MusicEntity>(this, arrayListOf(),R.layout.item_download){
            override fun bindViewHoler(holder: ContentViewHolder, position: Int, bean: MusicEntity) {
                //名字
                holder.setText(R.id.tv_locality_name,"${bean.name}")
                //进度
                holder.getView<View>(R.id.tv_locality_progress)?.visibility=View.GONE
                holder.getView<View>(R.id.pb)?.visibility=View.GONE
                //描述
                if(bean.downloadEntity!=null){
                    holder.setText(R.id.tv_locality_description,"${StringTools.toM(bean.downloadEntity?.fileSize!!)}M")
                }
                //删除
                holder.getView<ImageView>(R.id.iv_del_download)?.setOnClickListener {
                      if(isNotQuickClick()){
                         dele(bean.downloadEntity,holder.adapterPosition)
                      }
                }
            }
        }
        recy_my_download.addItemDecoration(RecyclerViewSpace(this,R.color.colorGray22,2))
        recy_my_download.adapter=downloadAdapter
    }

    /*
    *删除
    */
    private fun dele(entity: DownloadEntity?,position:Int){
        launch {
            entity?.let {
                //删除数据库
                AppDatabase.getAppDatabase().getMusicDao().deleteId(entity.downloadId)
                //删除file
                val file= File(Constant.downloadMusicPath+"${entity.title}.${entity.fileExtension}")
                if(file.exists()){
                    file.delete()
                }
                withContext(Dispatchers.Main){
                    downloadAdapter?.remove(position)
                }
            }
        }
    }


   inner class DownloadSucceedReceiver:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action){
                Constant.ACTION_SUCCEED->{
                    val entity = p1.getParcelableExtra<MusicEntity>("bean")
                    if(entity!=null){
                        downloadAdapter?.add(entity)
                    }
                }
            }
        }
    }

}
