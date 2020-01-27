package com.example.baidumusic2.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.fragments.MainFragment

class HomeBroadcastReceiver(var fragment: MainFragment):BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            if(p1.action==Constant.ACTINON_NOTIFY_HOME){
                val position=p1.getIntExtra("click_position",0)
                fragment.notifyAdapterChecked(position)
            }
        }
    }
}