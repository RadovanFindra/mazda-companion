package com.example.mazdacompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

   private val bluetoothService = BluetoothService()
   lateinit var bluetoothManager: MyBluetoothManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = MyBluetoothManager(this, bluetoothService)
        bluetoothManager.initialize()
        bluetoothManager.startDiscovery()

        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CompanionApp()
//                    MainScreen(
//                        onNotificationButtonClick = { /*TODO*/ },
//                        onDeviceClick = {},
//                        discoveredDevices = bluetoothManager.discoveredDevices
//                    )
                    }
                }
            }





//    private fun sendNotificationData() {
//        val json = NotificationListener.notificationsToJson()
//        bluetoothService.sendData(json)
//    }
//
//    private fun startPeriodicDataSend() {
//        CoroutineScope(Dispatchers.IO).launch {
//            while (true) {
//                delay(60000) // 1 minute
//                val data = JSONObject().put("preset", "default").put("timestamp", System.currentTimeMillis())
//                bluetoothService.sendData(data)
//            }
//        }
//    }
}

