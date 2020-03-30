package com.example.baidumusic2.udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*


/**
 * 建立UPD服务端
 *
 */
class UdpService {

    //创建自己服务端的地址
    private var intentAddress:InetAddress?=null
    //创建自己服务端的端口号
    private var port:Int=7777;
    //创建自己服务端的soket
    private var serviceSocket:DatagramSocket?=null
    //创建自己服务端的datapackage
    private var sendDatagraPackage:DatagramPacket?=null

    private val mScnner by lazy { Scanner(System.`in`) }

    init {

        try {
            mScnner.useDelimiter("\n")
            intentAddress=InetAddress.getLocalHost()
            serviceSocket= DatagramSocket(port,intentAddress)
        } catch (e: UnknownHostException) {
           println("服务端初始化异常${e}")
        }

    }

    fun start(){
        while (true){
            try {
                var buf=ByteArray(1024) //udp大小限制1024
                val receiverDatagramPacket = DatagramPacket(buf,0,buf.size)
                serviceSocket?.receive(receiverDatagramPacket)

                //服务端接收客户端的信息
                val receiverMsg= String(receiverDatagramPacket.data,0,receiverDatagramPacket.length)
                val receiverPort=receiverDatagramPacket.socketAddress
                println("服务端接收到:${receiverPort}--->${receiverMsg}")

                //服务端发送消息给客户端
                val next = mScnner.next()
                val sendMsgByte=next.toByteArray()
                sendDatagraPackage=DatagramPacket(sendMsgByte,0,sendMsgByte.size,receiverDatagramPacket.socketAddress)
                serviceSocket?.send(sendDatagraPackage)
            }catch (e:Exception){
                println("服务端异常${e}")
            }
        }
    }
}

//fun main(args: Array<String>) {
//    UdpService().start()
//}