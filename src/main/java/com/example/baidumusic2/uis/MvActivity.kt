package com.example.baidumusic2.uis

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.KeyEvent
import android.view.View
import com.example.baidumusic2.R
import com.example.baidumusic2.adapter.MvAdapter
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.bean.VideoBean
import com.example.baidumusic2.databinding.ActivityMvBinding
import com.example.baidumusic2.tools.MyLinearLayoutManager
import com.example.baidumusic2.tools.Screen
import kotlinx.android.synthetic.main.activity_mv.*
import kotlinx.android.synthetic.main.layout_title_toolbar.*

class MvActivity :MyBaseActivity<ActivityMvBinding>(){

    private val layoutManager by  lazy { MyLinearLayoutManager(this) }


    override fun getLayoutId(): Int {
        Screen.setStatusBar(this,true,false,R.color.colorBlack1)
        return R.layout.activity_mv
    }

    override fun business() {
        initToolbar()
        initRecy()
    }


    private fun initToolbar(){
        toolbar.setBackgroundColor(resources.getColor(R.color.colorBlack1))
        tv_toolbar_title.text="精彩视频"
        tv_toolbar_title.setTextColor(resources.getColor(R.color.colorWhite))
    }

    private fun initRecy(){
        val lists= arrayListOf<VideoBean>()
        var bean:VideoBean?=null
        for(i in 0..6){
            if(i==0){
                bean=VideoBean(i,Constant.videoUrl,0)
            }else{
                bean=VideoBean(i,Constant.videoUrl,1)
            }
            lists.add(bean)
        }

        val mvAdapter=MvAdapter(this,lists,recy_mv)
        recy_mv.adapter=mvAdapter
        recy_mv.layoutManager=layoutManager
        lifecycle.addObserver(mvAdapter)
    }


    /**
     * 横竖屏切换
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            println("横屏")
            dataBinding?.toolbar?.visibility=View.GONE
        }else{
            println("竖屏")
            dataBinding?.toolbar?.visibility=View.VISIBLE
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
               setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
               return true
            }
            startActivity(Intent(this,PlayActivity::class.java))
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }


}
