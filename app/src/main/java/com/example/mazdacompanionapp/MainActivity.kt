package com.example.mazdacompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme

class MainActivity : ComponentActivity() {

    private val bluetoothService = BluetoothService()
    private lateinit var bluetoothManager: MyBluetoothManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = MyBluetoothManager(this, bluetoothService)
        bluetoothManager.initialize()

        setContent {
            MazdaCompanionAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        onNotificationButtonClick = { sendNotificationData() },
                        onDeviceClick = { bluetoothManager.connectToDevice(it) },
                        discoveredDevices = bluetoothManager.discoveredDevices
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothManager.unregisterReceiver()
    }

    fun sendNotificationData() {
        val json = NotificationListener.notificationsToJson()
        bluetoothService.sendData(json)
    }

}
