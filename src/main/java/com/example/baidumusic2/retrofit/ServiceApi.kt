package com.example.baidumusic2.retrofit

import com.example.baidumusic2.bean.PlayBean
import com.example.baidumusic2.bean.SongListBean
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    //推荐新音乐 http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=1&size=10&offset=0
    @GET("v1/restserver/ting")
     fun getSongList(
            @Query("method") method:String,
            @Query(" type") type:String,
            @Query(" size") int: Int
    ):Flowable<SongListBean>



    @GET("v1/restserver/ting")
    fun getPlay(@Query("songid")songid:String,
                        @Query("method") method:String
    ):Flowable<PlayBean>

    @GET("v1/restserver/ting")
   suspend fun getPlaySource(@Query("songid")songid:String,
                @Query("method") method:String
    ):PlayBean
}