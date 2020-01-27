package com.example.baidumusic2.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.baidumusic2.R
import com.example.baidumusic2.tools.ActivityManage
import com.example.baidumusic2.tools.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class MyBaseActivity<T:ViewDataBinding>:AppCompatActivity(),CoroutineScope{

     var job:Job= Job()

    var dataBinding:T?=null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Screen.setStatusBar(this,false,true, R.color.colorWhite)
        dataBinding=DataBindingUtil.setContentView(this,getLayoutId())
        ActivityManage.add(this)
        getParameter()
        business()
    }


    abstract fun getLayoutId():Int

    abstract fun business()

    open fun getParameter(){}


    override fun onDestroy() {
        super.onDestroy()
        ActivityManage.remove(this)
        job.cancel()
    }

}