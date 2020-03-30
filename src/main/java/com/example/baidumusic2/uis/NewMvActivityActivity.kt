package com.example.baidumusic2.uis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baidumusic2.R
import com.example.baidumusic2.adapter.NewMvAdapter
import com.example.baidumusic2.bean.VideoBean
import com.example.baidumusic2.tools.Screen
import kotlinx.android.synthetic.main.activity_new_mv_activity.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class NewMvActivityActivity : AppCompatActivity() {

    private var adapter:NewMvAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Screen.setStatusBar(this,false,false,R.color.colorBlack1)
        setContentView(R.layout.activity_new_mv_activity)
        getData()
        initAdapter()
    }


    fun initAdapter(){
        recy_mv.layoutManager=LinearLayoutManager(this)
        adapter=NewMvAdapter(this, ArrayList<VideoBean>(),recy_mv)
        recy_mv.adapter=adapter
    }



    fun getData(){
        val url="http://music.163.com/api/mv/detail?id=10883448&type=mp4"
        val okHttpClient=OkHttpClient.Builder()
                .readTimeout(15*1000,TimeUnit.MILLISECONDS)
                .connectTimeout(15*1000,TimeUnit.MILLISECONDS)
                .build()
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                 println("访问失败--->${e.toString()}")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful&&response.code()==200){
                    if(response.body()!=null){
                        val result = JSONObject(response.body()!!.string())
                        val data = result.get("data") as JSONObject;
                        val brs=data.get("brs") as JSONObject
                        val mvUrl=brs.get("720") as String
                        val thumbUrl=data.get("cover") as String
                        val lists= arrayListOf<VideoBean>()
                        var bean:VideoBean?=null
                        for(i in 0..8){
                            bean=VideoBean(i,mvUrl,0,thumbUrl)
                            lists.add(bean)
                        }
                        runOnUiThread {
                            adapter?.addDatas(lists,true)
                        }
                    }
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.destory()
    }
}
