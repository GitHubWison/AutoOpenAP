package studio.dex.autoopenaplibrary

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import java.lang.Exception

object AutoOpenAp {
    var apManager: APManagerI = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
        AndroidOver7OpenAP()
    } else {
        AndroidUnder7OpenAP()
    }

    //    打开热点
    fun openAP(context: Context):Boolean {
       return apManager.openAP(context)
    }

    //    关闭热点
    fun closeAP() {
        apManager.closeAP()
    }
}

interface APManagerI {
    fun openAP(context: Context):Boolean
    fun closeAP()
}


class AndroidUnder7OpenAP : APManagerI {
    override fun openAP(context: Context):Boolean {
        var res:Boolean = false
        Log.e("7.0以下", "openAP")
        try {
            //        1.拿到热点名和密码
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val method = wifiManager.javaClass.getDeclaredMethod("getWifiApConfiguration")
            val config = method.invoke(wifiManager) as WifiConfiguration
            val ssid = config.SSID
            val preSharedKey = config.preSharedKey

            //        2.通过热点名和密码开启wifi热点
            //如果wifi处于打开状态，则关闭wifi,
            if (wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = false
            }
            val openConfig = WifiConfiguration().apply {
                this.SSID = ssid
                this.preSharedKey = preSharedKey
                allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED)//开放系统认证
                allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                allowedKeyManagement.set(4)
                allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP)
                allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP)
                this.status = WifiConfiguration.Status.ENABLED

            }
            val openMethod =
                wifiManager.javaClass.getMethod("setWifiApEnabled", WifiConfiguration::class.java, Boolean::class.java)
            res = openMethod.invoke(wifiManager, openConfig, true) as Boolean


        } catch (e: Exception) {
            e.printStackTrace()
            res = false
        }
        return res


    }

    override fun closeAP() {
        Log.e("7.0以下", "closeAP")
    }

}

class AndroidOver7OpenAP : APManagerI {
    override fun openAP(context: Context):Boolean {
        Log.e("7.0以上", "openAP")
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.startTethering(ConnectivityManager.TETHERING_WIFI,
            true, ONStartTetheringCallback())
        return true
    }

    override fun closeAP() {
        Log.e("7.0以上", "closeAP")
    }

}

 class ONStartTetheringCallback : ConnectivityManager.OnStartTetheringCallback()

