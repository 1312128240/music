package com.example.baidumusic2.udp

import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*

class UdpClient {

    private var intentAddress:InetAddress?=null
    private var servicePort:Int=7777;
    private var mStocket:DatagramSocket?=null
    private var sendDatagram:DatagramPacket?=null

    private val mScnner:Scanner by lazy { Scanner(System.`in`) }


    init {
        intentAddress= InetAddress.getByName("192.168.117.1")
        mStocket=DatagramSocket()
    }

    fun start(){
       while (true){
           try {
               //发送消息给服务端
               mScnner.useDelimiter("\n")
               val buf = mScnner.next().toByteArray()
               sendDatagram=DatagramPacket(buf,0,buf.size,intentAddress,servicePort)
               mStocket?.send(sendDatagram)

               //接收服务端的消息
               val byte=ByteArray(1024)
               val receiverDatagramPacket=DatagramPacket(byte,0,byte.size,intentAddress,servicePort)
               mStocket?.receive(receiverDatagramPacket)

               val receiverMsg = String(receiverDatagramPacket.data,0,receiverDatagramPacket.length)
               val address=receiverDatagramPacket.socketAddress
               println("客户端接收到:${address}--->${receiverMsg}")

           }catch (e:Exception){
               println("客户端异常")
           }
       }


    }
}

fun main(args: Array<String>) {
    var test=UdpClient().start()
}