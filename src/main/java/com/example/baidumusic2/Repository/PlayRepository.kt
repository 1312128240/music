package com.example.baidumusic2.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.bean.PlayBean
import com.example.baidumusic2.room.DownloadEntity
import com.example.baidumusic2.service.PlayService
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class PlayRepository {

  //  private var playLiveData=MutableLiveData<PlayBean>()



    /*
     *獲取音樂資源
     */
//    fun getResource(flowable: Flowable<PlayBean>):LiveData<PlayBean>{
//        val playLiveData=MutableLiveData<PlayBean>()
//         flowable.subscribeOn(Schedulers.io())
//                 .observeOn(AndroidSchedulers.mainThread())
//                 .subscribe(object :Subscriber<PlayBean>{
//                     override fun onComplete() {
//
//                     }
//
//                     override fun onSubscribe(s: Subscription) {
//                          s.request(Long.MAX_VALUE)
//                     }
//
//                     override fun onNext(t: PlayBean?) {
//                          playLiveData.value=t
//                     }
//
//                     override fun onError(t: Throwable?) {
//                          val playBean=PlayBean(null,500,t?.message.toString(),null)
//                          playLiveData.value=playBean
//                     }
//
//                 })
//        return playLiveData
//    }

}