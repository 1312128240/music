package com.example.baidumusic2.udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class  UDP_Service {

   private var datagramSocket:DatagramSocket?=null

   private val port=62025;

   private var inetAddress:InetAddress?=null

    private var  packet:DatagramPacket?=null

   fun start(listener:UPDListener){
       Thread(object :Runnable{
           override fun run() {
               inetAddress=InetAddress.getLocalHost()
               datagramSocket= DatagramSocket(port,inetAddress)
               println("服务端口号--->${InetAddress.getLocalHost()}")
               while (true){
                   try {
                       val buf= ByteArray(1024)
                       packet=DatagramPacket(buf,0,buf.size)
                       datagramSocket?.receive(packet)
                       val message= String(packet!!.data,0,packet!!.length)
                       println("服务端接收到消息---${message}")
                       listener.onMessage(message)
                   }catch (e:Exception){
                       println("服务端接收异常---${e.toString()}")
                   }

               }
           }
       }).start()
   }


    fun send(content:String){
        try {
            println("服务端发送消息--->${content}")
            val buffer=content.toByteArray()
            val datagramPacket=DatagramPacket(buffer,0,buffer.size,packet?.socketAddress)
            datagramSocket?.send(datagramPacket)
        }catch (e: java.lang.Exception){
            println("服务端发送异常---${e.toString()}")
        }
    }
}