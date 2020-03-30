package com.example.baidumusic2.adapter

import android.app.Activity
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.baidumusic2.R
import com.example.baidumusic2.bean.VideoBean
import com.example.baidumusic2.tools.Screen
import com.example.baidumusic2.views.MyVideoLayout
import kotlinx.android.synthetic.main.activity_new_mv_activity.*

class NewMvAdapter  constructor(var mContext:Activity,var lists:ArrayList<VideoBean>, recy:RecyclerView):RecyclerView.Adapter<NewMvAdapter.MvViewHolder>() {

    private var layoutLists=SparseArray<MyVideoLayout>()
    private var distanceY=0
    private var playPosition=-1

    init {
        recy.setOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState){
                    RecyclerView.SCROLL_STATE_IDLE ->{
                        val itemHeight= Screen.dip2px(320f)
                        var itemPosition =((distanceY*1.0) / itemHeight).toInt()
                        if(distanceY<(itemHeight/3)){
                            itemPosition=0
                        }else{
                            println("滑动算法--->${itemHeight}--->${itemPosition}---->${distanceY%itemHeight}")
                            if(distanceY%itemHeight>(itemHeight/4)){
                                itemPosition+=1
                            }
                        }
                        if(playPosition!=itemPosition){
                            println("on--->Scroll---->${playPosition}---->${itemPosition}")
                            if(playPosition!=-1){
                                layoutLists.get(playPosition).pausePlay(playPosition)
                            }
                            playPosition=itemPosition
                            layoutLists.get(itemPosition).startPlay(itemPosition)
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                distanceY+=dy
            }
        })
    }

    fun addDatas(list:ArrayList<VideoBean>,isClean:Boolean){
        if(isClean){
            lists.clear()
        }
        lists.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MvViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.item_mv,parent,false)
        return MvViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: MvViewHolder, position: Int) {
        val entity=lists[position]
        val layout = holder.getView<MyVideoLayout>(R.id.videoLayout)
        //layout添加数据
        layout.setDats(entity,position)
        //将layout保存进集合
        layoutLists.put(position,layout)
        //on
        layout.playingListener=object :MyVideoLayout.PlayingListener{
            override fun playing(position: Int) {
                if(playPosition!=position){
                    //recy.smoothScrollToPosition(position)
                    println("on--->ClickPlay---->${playPosition}---->${position}")
                    if(playPosition!=-1){
                        layoutLists.get(playPosition).pausePlay(playPosition)
                    }
                    playPosition=position
                }
            }
        }
    }


    class MvViewHolder constructor( itemView:View): RecyclerView.ViewHolder(itemView) {

        fun <T:View> getView(id:Int):T{
           return itemView.findViewById<T>(id)
        }
    }

    fun destory(){
        if(playPosition!=-1){
            layoutLists.get(playPosition).onDestory(playPosition)
        }
    }
}