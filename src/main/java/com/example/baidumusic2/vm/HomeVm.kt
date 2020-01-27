package com.example.baidumusic2.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.baidumusic2.Repository.SongListRepository
import com.example.baidumusic2.bean.SongListBean
import com.example.baidumusic2.retrofit.RetrofitUtil

class HomeVm(var parame:String):BaseViewModel(){

    private val  repository by lazy { SongListRepository() }

    fun getSongList(method:String,type:String,count:Int):LiveData<SongListBean>{
         val flowable= RetrofitUtil.getServiceApi().getSongList(method,type,count)
         return repository.getSongList(flowable);
     }

    class HomeFactory(val parame:String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T{
            return HomeVm(parame) as T
        }

    }
}