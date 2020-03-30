package com.example.baidumusic2.download

import android.content.Intent
import com.example.baidumusic2.MyApp
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.retrofit.OkhttpUtils
import com.example.baidumusic2.room.AppDatabase
import com.example.baidumusic2.room.DownloadEntity
import com.example.baidumusic2.room.MusicEntity
import com.example.baidumusic2.tools.ActivityManage
import com.example.baidumusic2.uis.LocalMusicActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

class DownloadManage constructor(var entity: DownloadEntity){


    var DownloadAction=Constant.DOWNLOADING


    init {
        okhttpDownload()
    }


    /**
     * okhttp下载
     */
    private fun okhttpDownload(){
        val okhttpClient = OkhttpUtils.getOkhttpClient()
        val request=Request.Builder()
                .url(entity.fileLink)
                .tag(entity.downloadId)
                .addHeader("RANGE", "bytes=" +entity.startPoistion+ "-"+entity.fileSize)
               // .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .build()
        okhttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                downloadError("onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    response.body()?.let {
                        val contentLength = it.contentLength()+entity.startPoistion
                        val inputStream = it.byteStream()
                        val buffer=ByteArray(1*1024)
                        var currentLength=0
                        var totalLength=entity.startPoistion
                        var progress=0;
                        //RandomAccessFile,并设置设置插入起点为startPoistion
                        val randoomFile=RandomAccessFile(getFile(),"rw")
                        randoomFile.seek(entity.startPoistion.toLong())
                        try {

                            do {
                                progress=(totalLength*100.0/contentLength).toInt()
                                entity.progress=progress
                                entity.startPoistion=totalLength
                               // entity.download_FilePath=randoomFile.toString()

                                when(DownloadAction){
                                    Constant.DOWNLOADING->{
                                        downloading()
                                        if(progress==100){
                                            return
                                        }
                                    }
                                    Constant.DOWNLOAD_CANCEL->{
                                        downloadCancel()
                                        return
                                    }
                                    Constant.DOWNLOAD_PAUSE->{
                                        downloadPause()
                                        return
                                    }
                                    Constant.DOWNLOAD_ERROR->{
                                        downloadError("do while")
                                        return
                                    }
                                }
                                //inputStream.read读取buffer的（off到length）个字节数
                                currentLength=inputStream.read(buffer,0,1*1024)
                                //将字节数组buffer从off位置开始到(currentlength)结尾写入到文件中
                                randoomFile.write(buffer,0,currentLength)
                                totalLength+=currentLength
                            }while (currentLength!=-1)

                        }catch (e:IOException){
                           // MyPrint.pln("${entity.name}-->下载捕捉异常--->${DownloadAction}--->${e.toString()}")
                            downloadError(e.toString())
                        }finally {
                            println("${entity.title}-->下载finally---->${DownloadAction}")
                            inputStream.close()
                            randoomFile.close()
                        }

                    }
                }else{
                    downloadError("response.is not successful---->${response.code()}")
                }

            }
        })
    }


    /**
     * 创建歌曲文件
     */
    private fun getFile():File{
        val dirFile=File(Constant.downloadMusicPath)
        if(!dirFile.exists()){
            dirFile.mkdirs()
        }
        val musicFile=File(dirFile,"${entity.title}.${entity.fileExtension}")
        if(!musicFile.exists()){
            musicFile.createNewFile()
        }
        return musicFile
    }


    /**
     * 下载中
     */
  private  fun  downloading(){
       // println("${entity.name}-->下载进度---->${entity.progress}%")
        //判断文件是否还存在，
        val file=File(Constant.downloadMusicPath+"${entity.title}.${entity.fileExtension}")
        if(!file.exists()){
            println("${entity.title}.${entity.fileExtension}文件夹被删除")
            DownloadAction=Constant.DOWNLOAD_CANCEL
            return
        }
        if(entity.progress!=100){
            entity.status=Constant.DOWNLOADING
            updateEntity()
        }else{
            OkhttpUtils.cancel(entity.downloadId)
            entity.status=Constant.DOWNLOAD_FINISH
            updateEntity()
            DownloadTask.removeTask(entity.downloadId)
        }
    }

    /**
     * 更新数据库
     */
    fun updateEntity(){
        val musicEntity=AppDatabase.getAppDatabase().getMusicDao().queryOne(entity.downloadId)
        musicEntity?.let {
            it.downloadEntity=entity
            AppDatabase.getAppDatabase().getMusicDao().updateAll(it)

            //发送广播
            if(entity.status==Constant.DOWNLOAD_FINISH){
                if(ActivityManage.getCurrentActivity() is LocalMusicActivity){
                    val intent=Intent("com.example.baidumusic2.uis.LocalMusicActivity.DownloadSucceedReceiver")
                    intent.putExtra("bean",it)
                    intent.action=Constant.ACTION_SUCCEED
                    MyApp.getContext().sendBroadcast(intent)
                }
            }

        }
    }

    /**
     * 从数据库中删除
     */
    fun cancelEntity(){
        AppDatabase.getAppDatabase().getMusicDao().deleteId(entity.downloadId)
    }


    /**
     * 暂停下载任务
     * 数据库更新为暂停状态
     * 下载队列中删除
     */
   fun downloadPause(){
        println("${entity.title}-->下载暂停")
        DownloadAction=Constant.DOWNLOAD_PAUSE
        entity.status=Constant.DOWNLOAD_PAUSE
        OkhttpUtils.cancel(entity.downloadId)
        //AppDatabase.getAppDatabase().getMusicDao().updateAll(entity)
        updateEntity()
        DownloadTask.removeTask(entity.downloadId)
    }


    /**
     * 取消下载任务
     * 从数据库删除
     * 从下载任务队列中删除
     */
    fun downloadCancel(){
        println("${entity.title}-->下载取消")
        DownloadAction=Constant.DOWNLOAD_CANCEL
        OkhttpUtils.cancel(entity.downloadId)
        entity.status=Constant.DOWNLOAD_CANCEL
       // AppDatabase.getAppDatabase().getMusicDao().deleteAll(entity)
        cancelEntity()
        DownloadTask.removeTask(entity.downloadId)
    }


    /**
     * 下载错误
     */
    private fun downloadError(error:String){
        println("${entity.title}-->下载错误--->${error}")
       // DownloadAction=Constant.DOWNLOAD_ERROR
        OkhttpUtils.cancel(entity.downloadId)
        entity.status=Constant.DOWNLOAD_ERROR
        updateEntity()
        DownloadTask.removeTask(entity.downloadId)
    }


}