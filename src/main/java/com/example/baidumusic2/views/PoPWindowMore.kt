package com.example.baidumusic2.views

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.example.baidumusic2.R
import com.example.baidumusic2.room.MusicEntity
import com.example.baidumusic2.tools.MyToast
import com.example.baidumusic2.tools.isNotQuickClick
import com.example.baidumusic2.uis.MvActivity

class PoPWindowMore constructor(var mContext:Context,var entity: MusicEntity?):PopupWindow()
           ,LifecycleObserver,View.OnClickListener{

   var listener:onClickListener?=null

    init {
        initView()
    }

    private fun initView(){
        val view=LayoutInflater.from(mContext).inflate(R.layout.popwindow_more,null)
        height=420
        width= ViewGroup.LayoutParams.MATCH_PARENT
        animationStyle=R.style.popwindAnimation
        isOutsideTouchable=true
        setBackgroundDrawable(ColorDrawable())
        contentView=view

        setView(view)
    }


    private fun setView(view:View){
        entity?.let {
            //头像
            val ivArt=view.findViewById<ImageView>(R.id.iv_music_more)
            Glide.with(mContext).load(it.pic_small).into(ivArt)
            //歌名
            val tvMusicName=view.findViewById<TextView>(R.id.tv_music_name)
            tvMusicName.text=it.name
            //演唱者
            val tvArtName=view.findViewById<TextView>(R.id.tv_music_artr_name)
            tvArtName.text=it.downloadEntity?.author
            //歌手
            val tvSinger=view.findViewById<TextView>(R.id.tv_music_more_singer)
            tvSinger.text="歌手: ${it.downloadEntity?.author}"
            //专辑
            val tvAlbum=view.findViewById<TextView>(R.id.tv_music_more_album)
            tvAlbum.text="专辑: ${it.name}"
            //地区语言
            val tvLanguage=view.findViewById<TextView>(R.id.tv_music_more_language)
            tvLanguage.text="地区语言: 华语"
            //出品公司
            val tvWindowMv=view.findViewById<TextView>(R.id.tv_music_more_company)
          //  tvCompany.text="出品公司: 北京万上文化传媒有限公司"
            //看mv
            val tvMv=view.findViewById<TextView>(R.id.tv_music_more_mv)
            tvMv.setOnClickListener (this)
            tvWindowMv.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        if(isNotQuickClick()){
            when(p0?.id){
                R.id.tv_music_more_mv->{
                    listener?.clickMV()
                    //dismiss()
                    //mContext.startActivity(Intent(mContext,MvActivity::class.java))
                }
                R.id.tv_music_more_company->{
                    listener?.windowMV()
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy(){
        dismiss()
    }


    interface onClickListener{
        fun windowMV()
        fun clickMV()
    }

}