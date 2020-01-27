package com.example.baidumusic2.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

object OkhttpUtils {

    private var okhttpClient:OkHttpClient?=null

    fun getOkhttpClient():OkHttpClient{
        if(okhttpClient==null){
            okhttpClient =OkHttpClient()
                    .newBuilder()
                    .addInterceptor(getInterceptor())
                    .readTimeout(15*1000,TimeUnit.MILLISECONDS)
                    .writeTimeout(15*1000,TimeUnit.MILLISECONDS)
                    .connectTimeout(15*1000,TimeUnit.MILLISECONDS)
                    .build()
        }
        return okhttpClient as OkHttpClient
    }

   private  fun getInterceptor()=object :Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                    .removeHeader("User-Agent")
//                    .addHeader("RANGE", "bytes=" +startPosition + "-")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                    .build()
            val response=chain.proceed(newRequest)
            val bodyString=response.body().toString()

            println("请求方式--->${newRequest.method()}")
            println("请求头----->${newRequest.headers()}")
            println("请求Url--->${newRequest.url()}")
            println("返回内容-->${bodyString}")

            return chain.proceed(newRequest)
        }
    }

     fun cancel(tag:String){
        val queuedCalls = getOkhttpClient().dispatcher().queuedCalls()
        println("队列中okhttp大小--->${queuedCalls.size}")
        for (call in queuedCalls){
            if(call.request().tag()==tag){
                 call.cancel()
            }
        }
        val runingCalls= getOkhttpClient().dispatcher().runningCalls()
        println("运行中okhttp队列大小--->${queuedCalls.size}")
        for (call in runingCalls){
            if(call.request().tag()==tag){
                call.cancel()
            }
        }
    }
}