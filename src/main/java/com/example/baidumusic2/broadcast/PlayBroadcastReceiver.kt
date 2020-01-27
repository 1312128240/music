package com.example.baidumusic2.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.baidumusic2.base.Constant
import com.example.baidumusic2.uis.PlayActivity

class PlayBroadcastReceiver constructor(var activity:PlayActivity):BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            if(it.action==Constant.ACTION_COMPLETION){
                activity.playNextAndPrevious(true)
            }else if(it.action==Constant.ACTINON_PREPARED){
                activity.prepared()
            }
        }
    }
}