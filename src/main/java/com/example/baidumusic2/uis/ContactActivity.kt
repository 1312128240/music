package com.example.baidumusic2.uis

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.baidumusic2.MainActivity
import com.example.baidumusic2.R
import com.example.baidumusic2.base.MyBaseActivity
import com.example.baidumusic2.tools.SPTools
import com.example.baidumusic2.udp.UDP_Client
import com.example.baidumusic2.udp.UDP_Service
import com.example.baidumusic2.udp.UPDListener
import com.example.baidumusic2.udp.UdpClient
import com.example.baidumusic2.views.MySwichView
import kotlinx.android.synthetic.main.activity_contact.*


class ContactActivity : MyBaseActivity<com.example.baidumusic2.databinding.ActivityContactBinding>(){

    override fun getLayoutId(): Int {
        return R.layout.activity_contact
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("切换主题onSaveInstanceState")
    }

    override fun business() {
       // client() //左

     //   service()  //右

      //  udpClient()


        swichView.listener=object :MySwichView.OnSwichChangeListener{
            override fun OnChange(b: Boolean) {
                val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
                    //delegate.localNightMode=AppCompatDelegate.MODE_NIGHT_YES
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SPTools.setAppTheme(true)

                }else{
                    //delegate.localNightMode= AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SPTools.setAppTheme(false)
                }
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }


    fun setTitle(title:String){
        toolbar.findViewById<TextView>(R.id.tv_toolbar_title).text=title;
    }

   private var client:UdpClient?=null



    fun client(){
        toolbar.findViewById<TextView>(R.id.tv_toolbar_title).text="客户端"
        val client=UDP_Client()
        client.start(object :UPDListener{
            override fun onMessage(content: String) {
                runOnUiThread(object :Runnable{
                    override fun run() {
                        tv_message.text=content
                    }
                })
            }
        })

        btn_send.setOnClickListener {
            val sendMessage=et.text.toString()
            client.send(sendMessage)
        }

    }



    fun service(){
        toolbar.findViewById<TextView>(R.id.tv_toolbar_title).text="服务端"
        val service= UDP_Service()

        service.start(object :UPDListener{
            override fun onMessage(content: String) {
               runOnUiThread(object :Runnable{
                   override fun run() {
                       tv_message.text=content
                   }
               })
            }
        })

        btn_send.setOnClickListener {
            val sendMessage=et.text.toString()
            service.send(sendMessage)
        }

    }
}
