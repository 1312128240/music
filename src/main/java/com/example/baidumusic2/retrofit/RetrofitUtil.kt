package com.example.baidumusic2.retrofit

import com.example.baidumusic2.base.Constant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    //https : //api.some-server.com/
   private fun getRetrofit():Retrofit{
        return Retrofit
                .Builder()
                .baseUrl(Constant.baseUrl)
                .client(OkhttpUtils.getOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .build()

    }


    fun getServiceApi():ServiceApi{
        return getRetrofit().create(ServiceApi::class.java)
    }


}