package com.example.baidumusic2.broadcast
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.baidumusic2.uis.PlayActivity

class NotifyBroadcastReceiver constructor(var activity:PlayActivity?):BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {

        p1?.let {
            if(it.action=="com.example.baidumusic2.notify.next"){
                  activity?.playNextAndPrevious(true)
            }else if(it.action=="com.example.baidumusic2.notify.previous"){
                  activity?.playNextAndPrevious(false)
            }else if(it.action=="com.example.baidumusic2.notify.pause"){
                  activity?.pause(activity?.dataBinding?.musicEntity!!)
            }else {
                //if(it.action=="com.example.baidumusic2.notify.restart")
                  activity?.restart(activity?.dataBinding?.musicEntity!!)
            }
        }

    }
}