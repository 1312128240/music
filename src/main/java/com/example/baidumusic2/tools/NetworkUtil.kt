package com.example.baidumusic2.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

class NetworkUtil {


    companion object{


        //没有网络连接
        private val NETWORN_NONE = 0;
        //wifi连接
        private val NETWORN_WIFI = 1;
        //手机网络数据连接类型
        private val NETWORN_2G = 2;
        private val NETWORN_3G = 3;
        private val NETWORN_4G = 4;
        private val NETWORN_MOBILE = 5;



        /**
         * 获取当前网络连接类型
         *
         * @param context
         * @return
         */
        fun getNetworkType( context:Context?):Int{
            //获取系统的网络服务
            val connManager =context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?;

            //如果当前没有网络
            if (null == connManager){
                return NETWORN_NONE;
            }

            //获取当前网络类型，如果为空，返回无网络
            val activeNetInfo = connManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return NETWORN_NONE;
            }

            // 判断是不是连接的是不是wifi
            val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (null != wifiInfo) {
                val state = wifiInfo.getState();
                if (null != state)
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        return NETWORN_WIFI;
                    }
            }

            // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
            val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != networkInfo) {
                val state = networkInfo.getState();
                val strSubTypeName = networkInfo.getSubtypeName();
                if (null != state)
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        when (activeNetInfo.getSubtype()) {

                            //如果是2g类型
                            TelephonyManager.NETWORK_TYPE_GPRS,
                            TelephonyManager.NETWORK_TYPE_CDMA,
                            TelephonyManager.NETWORK_TYPE_EDGE,
                            TelephonyManager.NETWORK_TYPE_1xRTT,
                            TelephonyManager.NETWORK_TYPE_IDEN->{ return NETWORN_2G; }

                            //如果是3g类型
                            TelephonyManager.NETWORK_TYPE_EVDO_A,
                            TelephonyManager.NETWORK_TYPE_UMTS,
                            TelephonyManager.NETWORK_TYPE_EVDO_0,
                            TelephonyManager.NETWORK_TYPE_HSDPA,
                            TelephonyManager.NETWORK_TYPE_HSUPA,
                            TelephonyManager.NETWORK_TYPE_HSPA,
                            TelephonyManager.NETWORK_TYPE_EVDO_B,
                            TelephonyManager.NETWORK_TYPE_EHRPD,
                            TelephonyManager.NETWORK_TYPE_HSPAP->{ return NETWORN_3G;}

                            //如果是4g类型
                            TelephonyManager.NETWORK_TYPE_LTE->{ return NETWORN_4G}

                            else->{
                                //中国移动 联通 电信 三种3G制式
                                if (strSubTypeName=="TD-SCDMA"|| strSubTypeName=="WCDMA"|| strSubTypeName=="CDMA2000") {
                                    return NETWORN_3G;
                                } else {
                                    return NETWORN_MOBILE;
                                }
                            }
                        }
                    }
            }
            return NETWORN_NONE;
        }


    }

}