package com.example.baidumusic2.uis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baidumusic2.R
import com.example.baidumusic2.adapter.VideoAdapter
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {

   //  var sensorManager: SensorManager?=null
   //  var sensorEventListener: Jzvd.JZAutoFullscreenListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        initAdapter()
       //initNewAdapter()
    }

    fun initAdapter(){
        val lists=ArrayList<String>()
        for(i in 0 until 10){
            lists.add("第${i}个")
        }
        val adapter=VideoAdapter(this,lists)
        listView.adapter=adapter
    }


}
