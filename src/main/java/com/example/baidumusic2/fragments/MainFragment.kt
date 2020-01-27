package com.example.baidumusic2.fragments


import android.content.Intent
import android.content.IntentFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.baidumusic2.R
import com.example.baidumusic2.adapter.BannerAdapter
import com.example.baidumusic2.adapter.BaseRecyclerAdapter
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.base.MyBaseFragment
import com.example.baidumusic2.bean.Song
import com.example.baidumusic2.bean.SongListBean
import com.example.baidumusic2.broadcast.HomeBroadcastReceiver
import com.example.baidumusic2.databinding.FragmentMainBinding
import com.example.baidumusic2.uis.PlayActivity
import com.example.baidumusic2.views.FlexViewGroup
import com.example.baidumusic2.views.RecyclerViewSpace
import com.example.baidumusic2.vm.HomeVm
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment :MyBaseFragment<FragmentMainBinding>(){

    private val homeVm by lazy {
        ViewModelProviders.of(this,HomeVm.HomeFactory("我是首页")).get(HomeVm::class.java)
    }

    private val homeReceiver by  lazy { HomeBroadcastReceiver(this) }

    private var adapter:BaseRecyclerAdapter<Song>?=null

    private var  bannerAdapter:BannerAdapter?=null

    private var lastCheckPosition=-1

    override fun getLayoutId(): Int {

       return R.layout.fragment_main
    }


    override fun business() {
        getMusickListDatas()
        register()
    }

    fun getMusickListDatas(){
        homeVm.getSongList(Constant.method_songlist,"1",15)
                .observe(this,object:Observer<SongListBean>{
                    override fun onChanged(t: SongListBean?) {
                        t?.song_list?.let {
                            initAdapter(it)
                        }
                    }
                })
    }

    fun register(){
        val inflater=IntentFilter()
        inflater.addAction(Constant.ACTINON_NOTIFY_HOME)
        mContext.registerReceiver(homeReceiver,inflater)
    }

    private  fun initAdapter(dataList:ArrayList<Song>){
        adapter=object : BaseRecyclerAdapter<Song>(mContext, dataList,R.layout.item_music){
            override fun bindViewHoler(holder: ContentViewHolder, position: Int, bean: Song) {
                val tvPositon = holder.itemView.findViewById<TextView>(R.id.tv_music_position)
                val tvName=holder.itemView.findViewById<TextView>(R.id.tv_music_title)
                val ivHorn=holder.itemView.findViewById<ImageView>(R.id.iv_horn)
                tvName.text=bean.title
                tvName.tag=position
                if(bean.isChecked&&tvName.tag==position){
                    tvPositon.visibility=View.GONE
                    ivHorn.visibility=View.VISIBLE
                    tvName.setTextColor(mContext.resources.getColor(R.color.colorRed))
                }else{
                    ivHorn.visibility=View.GONE
                    tvPositon.visibility=View.VISIBLE
                    tvPositon.text=position.toString()
                    tvName.setTextColor(resources.getColor(R.color.colorBlack1))
                }
                holder.setText(R.id.tvtv_music_author,bean.author)
            }

        }

        recy_home.addItemDecoration(RecyclerViewSpace(mContext,R.color.colorGray22,1,true))
        val headerView=LayoutInflater.from(mContext).inflate(R.layout.view_home_header,recy_home,false)
        recy_home.adapter=adapter
        adapter?.headerView=headerView
        adapter?.listener=object :BaseRecyclerAdapter.OnItemClickListener<Song>{
            override fun OnClick(position: Int, bean: Song,lists:ArrayList<Song>) {
                val intent= Intent(mContext,PlayActivity::class.java)
                intent.putExtra("musicList",lists)
                intent.putExtra("position",position)
                startActivity(intent)
            }

        }

        //头部
        initHeaderView(headerView,dataList)
    }


    /**
     * 头部
     */
    private fun initHeaderView(headerView:View,dataList: ArrayList<Song>){
        //轮播图
        val Vp=headerView.findViewById<ViewPager>(R.id.vp_home)
        val RgIndicator=headerView.findViewById<RadioGroup>(R.id.rg_indicator)
        val imaList= arrayListOf<String>()
        for ((i1,i2) in  dataList.withIndex()){
            if(i1<5){
                imaList.add(i2.pic_radio)
            }
        }
        imaList.add(0,imaList[4])
        imaList.add(imaList.size-1,imaList[0])
        bannerAdapter=BannerAdapter(mContext,imaList,Vp)
        Vp.adapter=bannerAdapter
        Vp.setCurrentItem(1,false)
        bannerAdapter?.bindViewPage()
        bannerAdapter?.bindIndicator(RgIndicator,Gravity.END)
        bannerAdapter?.onStart()

        //参数： type = 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,
        // 23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜
        val typeLayout=headerView.findViewById<FlexViewGroup>(R.id.flexlayout)
        typeLayout.setDatas()
        typeLayout.listener=object :FlexViewGroup.OnFlexClickListener{
            override fun onClick(key: Int, vaule: String) {
                println("点击的是-->${key}-->${vaule}")
            }

        }
    }

    fun notifyAdapterChecked(position:Int){
//        println("准备更新歌曲列表--->${position}--->${lastCheckPosition}")
        if(lastCheckPosition!=-1){
            adapter?.getDatas()?.get(lastCheckPosition)?.isChecked=false
            adapter?.notifyItemChanged(lastCheckPosition+1)
        }
        //因为有头部需要+1
        adapter?.getDatas()?.get(position)?.isChecked=true
        adapter?.notifyItemChanged(position+1)
        lastCheckPosition=position
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden) bannerAdapter?.onStop() else bannerAdapter?.onStart()
    }


    override fun onStop() {
        super.onStop()
        bannerAdapter?.onStop()
    }

    override fun onResume() {
        super.onResume()
        bannerAdapter?.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mContext.unregisterReceiver(homeReceiver)
    }
}
