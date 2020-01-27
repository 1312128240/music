package com.example.baidumusic2

import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.baidumusic2.animation.ViewRotateAnimation
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.broadcast.MainBroadcastReceiver
import com.example.baidumusic2.fragments.MainFragment
import com.example.baidumusic2.fragments.MeFragment
import com.example.baidumusic2.tools.MyToast
import com.example.baidumusic2.uis.MvActivity
import com.example.baidumusic2.uis.PlayActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MyBaseActivity<com.example.baidumusic2.databinding.ActivityMainBinding>(){

    private var lastShowFragment:Fragment?=null

    private val mainReceiver by  lazy { MainBroadcastReceiver(this) }

    private val rotateAnimation by lazy { ViewRotateAnimation(civ) }
    private var lastTime=0L

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun business() {
        registerMyReceiver()
        inBottomNavigation()
    }

    override fun onStart() {
        onBackFront()
        super.onStart()
    }

    /**
     * 从后台返回前台
     */
    private fun onBackFront(){
        when(MyApp.backFlag){
            0 -> {
                println("从其它Activity进入前台")
            }
            1->{
                println("从PlayActivity进入前台")
                startActivity(Intent(this,PlayActivity::class.java))
                MyApp.backFlag=-1
            }
        }
    }



   private fun registerMyReceiver(){
        val intentFilter=IntentFilter()
        intentFilter.addAction(Constant.ACTION_MAIN_ANIMATION_START)
        intentFilter.addAction(Constant.ACTION_MAIN_ANIMATION_PAUSE)
        intentFilter.addAction(Constant.ACTION_MAIN_ANIMATION_RESTART)
        registerReceiver(mainReceiver,intentFilter)
    }


    fun bottomAmiation(pic:String,action:String){
        Glide.with(this).load(pic).error(R.mipmap.ic_launcher_round).into(civ)
        when(action){
            Constant.ACTION_MAIN_ANIMATION_START->rotateAnimation.startAnimation()
            Constant.ACTION_MAIN_ANIMATION_PAUSE->rotateAnimation.pauseAnimation()
            Constant.ACTION_MAIN_ANIMATION_RESTART->rotateAnimation.restartAnimation()
        }
    }


   private fun inBottomNavigation(){
        val homeFragment=MainFragment()
        val meFragment=MeFragment()
        navigation.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when(p1){
                    R.id.rb_home->swichFragment(homeFragment)
                    R.id.rb_me->swichFragment(meFragment)
                }
            }

        })
        rb_home.isChecked=true
    }


   private fun swichFragment(showFragment: Fragment){

         if(lastShowFragment!=null){
             if(showFragment.isAdded){
                 supportFragmentManager.beginTransaction().hide(lastShowFragment!!).show(showFragment).commit()
             }else{
                 supportFragmentManager.beginTransaction().add(R.id.container,showFragment).hide(lastShowFragment!!).commit()
             }
         }else{
             supportFragmentManager.beginTransaction().add(R.id.container,showFragment).commit()
         }
         lastShowFragment=showFragment
     }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-lastTime>2000){
                MyToast.short("再按一次将退出应用")
                lastTime=System.currentTimeMillis()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mainReceiver)
    }

}
