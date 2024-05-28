package com.example.mazdacompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mazdacompanionapp.ui.CompanionApp

class MainActivity : ComponentActivity() {

    //private val events = mutableStateListOf<Event>()
    //private var selectedPreset by mutableStateOf<Preset?>(null)
    //private val bluetoothService = BluetoothService()
    //private lateinit var bluetoothManager: MyBluetoothManager
    //private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bluetoothManager = MyBluetoothManager(this, bluetoothService)
        //bluetoothManager.initialize()
        setContent {
            CompanionApp()
        }
    }
    //override fun onDestroy() {
    //    super.onDestroy()
    //    bluetoothManager.unregisterReceiver()
   // }
}





