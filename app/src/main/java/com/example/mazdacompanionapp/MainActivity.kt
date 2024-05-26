package com.example.mazdacompanionapp

import android.app.Notification
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme

val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

class MainActivity : ComponentActivity() {
    private val notificationListener = NotificationListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MazdaCompanionAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Button(onClick = {
                            val notificationCount = notificationListener.getNotificationCount()
                            // Trigger sending of notification count here
                        }) {
                            Text("Send Notification Count")
                        }
                    }
                }
            }
        }
    }
}

class NotificationListener : NotificationListenerService() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var notificationCount = 0

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        notificationCount++
    }

    fun getNotificationCount(): Int {
        return notificationCount
    }
}

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MazdaCompanionAppTheme {
            Greeting("Android")
        }
    }
