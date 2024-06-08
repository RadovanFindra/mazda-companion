package com.example.mazdacompanionapp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhoneInfoManager(private val context: Context) : ViewModel() {

    private var cellularSignalStrength: Int? = null

    init {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(object : PhoneStateListener() {
            @Deprecated("Deprecated in Java")
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
                super.onSignalStrengthsChanged(signalStrength)
                cellularSignalStrength = signalStrength?.level
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
    }
    fun getCurrentTime(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(currentTimeMillis))
    }

    fun getCellularSignalStrength(): Int? {
        return cellularSignalStrength
    }

    fun getWifiSignalStrength(): Int {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
    }

    fun getBatteryPercentage(): Int {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return if (level != -1 && scale != -1) {
            (level / scale.toFloat() * 100).toInt()
        } else {
            -1
        }
    }

    fun printPhoneInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = getCurrentTime()
            val cellularSignal = getCellularSignalStrength()
            val wifiSignal = getWifiSignalStrength()
            val battery = getBatteryPercentage()

            println("Time: $currentTime")
            println("Cellular Strength: ${cellularSignal}")
            println("Wi-Fi Strength: $wifiSignal")
            println("Battery: $battery%")
        }
    }
    fun createPhoneInfoJson(
    ): JSONObject {
        val currentTime = getCurrentTime()
        val cellularSignal = getCellularSignalStrength()
        val wifiSignal = getWifiSignalStrength()
        val battery = getBatteryPercentage()
        val phoneInfoJson = JSONObject()

        phoneInfoJson.put("Time", currentTime)
        phoneInfoJson.put("Cellular", cellularSignal)
        phoneInfoJson.put("Wi-Fi", wifiSignal)
        phoneInfoJson.put("Battery", battery)
        return phoneInfoJson
    }
}