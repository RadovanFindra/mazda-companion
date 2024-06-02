package com.example.mazdacompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MazdaCompanionAppTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    CompanionApp()
                }
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

