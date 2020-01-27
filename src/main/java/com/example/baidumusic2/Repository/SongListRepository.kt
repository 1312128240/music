package com.example.baidumusic2.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baidumusic2.bean.SongListBean
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class SongListRepository {

    private var songListLiveData=MutableLiveData<SongListBean>()

    fun getSongList(flowable: Flowable<SongListBean>):LiveData<SongListBean>{
        flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Subscriber<SongListBean>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(s: Subscription?) {
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(t: SongListBean) {
                        songListLiveData.value=t
                    }

                    override fun onError(t: Throwable?) {
                        val errorBean=SongListBean(500,t.toString(),null)
                        songListLiveData.value=errorBean
                    }

                })
        return songListLiveData
    }


}