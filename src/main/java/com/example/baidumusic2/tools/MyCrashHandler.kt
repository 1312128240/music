package com.example.baidumusic2.tools

import android.os.Handler
import android.os.Looper
import android.os.Message


object MyCrashHandler{


    private var mainHandler:Handler?=object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1->{
                    MyToast.short(msg.obj as String)
                }
            }
        }
    }

    fun install(){

        /**
         * /主线程的异常会从这里抛出
         * NullPointerException，ArithmeticException等
         */
        mainHandler?.post(Runnable {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    MyPrint.pln("主线程异常-->${e}")
                    sendMessage1(Thread.currentThread(),e,"")
                }
            }
        })

        /**
         * 线程未捕获异常处理器，捕获全局的所有线程的异常，用来处理未捕获异常。
         * 如果程序出现了未捕获异常，默认会弹出系统中强制关闭对话框。
         * 我们需要实现此接口，为程序中默认未捕获异常处理。这样当未捕获异常发生时，就可以做一些个性化的异常处理操作。
         */
        Thread.setDefaultUncaughtExceptionHandler(object :Thread.UncaughtExceptionHandler{
            override fun uncaughtException(p0: Thread, p1: Throwable) {
                MyPrint.pln("子线程异常-->${p1}")
                try {
                    sendMessage2("玩鸡儿,很抱歉,程序出现异常,即将退出.")
                    Thread.sleep(3000)
                } finally {
                    ActivityManage.exit()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            }
        })
    }

    /**
     * 提示消息
     */
    private fun sendMessage1(thread: Thread,throwable: Throwable,msg:String){
        val message=Message.obtain()
        message.what=1
        message.obj="${thread.name}--异常原因${throwable}\n${msg}"
        mainHandler?.sendMessage(message)
    }



    /**
     * 退出提示
     */
    private fun sendMessage2(msg:String){
        val message=Message.obtain()
        message.what=1
        message.obj=msg
        mainHandler?.sendMessage(message)
    }



    /**
     * 清空hnadler
     */
    fun unInstall(){
       if(mainHandler!=null){
           mainHandler!!.removeMessages(1)
           mainHandler=null
       }
    }
}