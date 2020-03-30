package com.example.baidumusic2.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.example.baidumusic2.R
import com.example.baidumusic2.base.MyBaseFragment
import com.example.baidumusic2.databinding.FragmentMeBinding
import com.example.baidumusic2.tools.SPTools
import com.example.baidumusic2.uis.ContactActivity
import com.example.baidumusic2.uis.DownloadActivity
import com.example.baidumusic2.uis.LocalMusicActivity


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

    fun myContact(view:View){
        startActivity(Intent(mContext,ContactActivity::class.java))
    }

    fun addBadge(view: View){
        try {
            val launchClassName = getLauncherClassName();
            println("启动类--->${launchClassName}")
            if (TextUtils.isEmpty(launchClassName)) {
                return
            }
            val bundle =Bundle();
            bundle.putString("package", context?.getPackageName());
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", 10);
            context?.getContentResolver()?.call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        } catch (e:Exception) {
            e.printStackTrace();
        }
    }


    fun  getLauncherClassName():String{
        val launchComponent = getLauncherComponentName(context!!);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

   fun getLauncherComponentName(context: Context): ComponentName?{
        val launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }

}
