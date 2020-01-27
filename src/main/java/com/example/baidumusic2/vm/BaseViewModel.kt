package com.example.baidumusic2.vm

import androidx.lifecycle.AndroidViewModel
import com.example.baidumusic2.MyApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel:AndroidViewModel(MyApp()),CoroutineScope{

    var job:Job= Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+job


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}