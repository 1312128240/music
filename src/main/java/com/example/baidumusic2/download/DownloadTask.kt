package com.example.baidumusic2.download

import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

object DownloadTask:CoroutineScope{

    private var job = Job() as Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+ job

    private val taskMap=HashMap<String,DownloadManage>()

    fun addTask(key:String,vaule:DownloadManage){
        taskMap.put(key,vaule)
    }

    fun removeTask(key: String){
        var manage= taskMap[key]
        if(manage!=null){
            manage=null
        }
        taskMap.remove(key)
    }

    fun getTask(key: String):DownloadManage?{
        return taskMap.get(key)
    }


    /**
     * 暂停okhttp的下载
     * 将未完成的任务更新为暂停
     *
     */
    fun init(){
        job=launch {
            val list=AppDatabase.getAppDatabase().getMusicDao().queryUnfinished()
            for (enity in  list.withIndex()){
                enity.value.downloadEntity?.status=Constant.DOWNLOAD_PAUSE
                AppDatabase.getAppDatabase().getMusicDao().updateAll(enity.value)
            }
           job.cancel()
        }
    }


}