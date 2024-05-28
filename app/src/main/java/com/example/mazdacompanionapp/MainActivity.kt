package com.example.mazdacompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private val events = mutableStateListOf<Event>()
    private var selectedPreset by mutableStateOf<Preset?>(null)
    private val bluetoothService = BluetoothService()
    private lateinit var bluetoothManager: MyBluetoothManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = MyBluetoothManager(this, bluetoothService)
        bluetoothManager.initialize()

        setContent {
            MazdaCompanionAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screens.MainScreen.route) {
                        composable(Screens.MainScreen.route) {
                            MainEventScreen(
                                onAddEventClick = { navController.navigate(Screens.AddEventScreen.route) },
                                events = events,
                                onEventClick = { /* handle event click if needed */ }
                            )
                        }
                        composable(Screens.AddEventScreen.route) {
                            AddNewEventScreen(
                                onSaveEvent = {
                                    events.add(it)
                                    navController.popBackStack()
                                },
                                onPresetClick = { navController.navigate(Screens.PresetSelectionScreen.route) },
                                selectedPreset = selectedPreset
                            )
                        }
                        composable(Screens.PresetSelectionScreen.route) {
                            PresetSelectionScreen(onPresetSelected = {
                                selectedPreset = it
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothManager.unregisterReceiver()
    }

    private fun sendNotificationData() {
        val json = NotificationListener.notificationsToJson()
        bluetoothService.sendData(json)
    }

    private fun startPeriodicDataSend() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(60000) // 1 minute
                val data = JSONObject().put("preset", "default").put("timestamp", System.currentTimeMillis())
                bluetoothService.sendData(data)
            }
        }
    }
}

