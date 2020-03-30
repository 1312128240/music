package com.example.baidumusic2.udp

import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InterfaceAddress

class UDP_Client {

    var datagramSocket:DatagramSocket?=null
    var inetAddress:InetAddress?=null


    fun start(listener:UPDListener){
        Thread(object :Runnable{
            override fun run() {
                inetAddress= InetAddress.getByName("127.0.0.1")
                datagramSocket= DatagramSocket()
                while (true){
                  try {
                      val buf=ByteArray(1024)
                      val datagramPacket= DatagramPacket(buf,0,buf.size,inetAddress,62025)
                      datagramSocket?.receive(datagramPacket)

                      val message= String(datagramPacket.data,0,datagramPacket.length)
                      println("客户端接收信息---${message}")
                      listener.onMessage(message)
                  }catch (e:Exception){
                      println("客户端接收异常---${e.toString()}")
                  }
                }
            }
        }).start()

    }

    fun send(content:String){
        try {
            println("客户端发送消息--->${content}")
            val buffer=content.toByteArray()
            val datagramPacket=DatagramPacket(buffer,0,buffer.size,inetAddress,62025)
            datagramSocket?.send(datagramPacket)
        }catch (e:Exception){
            println("客户端发送异常---${e.toString()}")
        }

    }


}