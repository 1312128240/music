package com.example.baidumusic2.fragments


import android.content.Intent
import android.view.View
import com.example.baidumusic2.R
import com.example.baidumusic2.base.MyBaseFragment
import com.example.baidumusic2.databinding.FragmentMeBinding
import com.example.baidumusic2.uis.DownloadActivity
import com.example.baidumusic2.uis.LocalMusicActivity
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.fragment_me.*


class MeFragment : MyBaseFragment<FragmentMeBinding>(){

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun business() {
        fragmentBind?.fragment=this
    }

    fun myDownload(view: View){
        startActivity(Intent(mContext,DownloadActivity::class.java))
    }


    fun myLocal(view: View){
        startActivity(Intent(mContext,LocalMusicActivity::class.java))
    }

}
