package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.Bluetooth.MyBluetoothManager
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.SEND_EVENT_PRESET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BluetoothSender(
    private val deviceItemsRepository: DeviceItemsRepository,
    private val bluetoothManager: MyBluetoothManager
) {
    private val _devices = MutableStateFlow<List<DeviceItem>>(emptyList())
    val devices: StateFlow<List<DeviceItem>> = _devices.asStateFlow()
    val periodicSender:PeriodicalSender = PeriodicalSender(bluetoothManager)


    init {
        CoroutineScope(Dispatchers.IO).launch {
            deviceItemsRepository.getAllDeviceItemsStream().collect { deviceItems ->
                _devices.value = deviceItems
            }
        }
    }

    fun startSending() {
        devices.value.forEach { deviceItem ->
            if (deviceItem.isEnabled) {
                deviceItem.events.forEach { event ->
                    if (event.isEnabled) {
                        when (event.preset) {
                            SEND_EVENT_PRESET.DEFAULT ->periodicSender.AddToSender(event, deviceItem)
                            SEND_EVENT_PRESET.ON_CONNECT -> println()
                            SEND_EVENT_PRESET.ON_NOTIFICATION_CHANCE -> println()
                            else -> {
                                // Handle other presets if necessary
                            }
                        }
                    }
                }
            }
        }
        periodicSender.StartSender()
    }



//    fun startPeriodicDataSend() {
//        CoroutineScope(Dispatchers.IO).launch {
//            while (true) {
//                delay(6000)
//                sendToDevices()
//            }
//        }
//    }
//
//    private fun sendToDevices() {
//        val notifications = NotificationListener.notificationsToJson()
//        devices.value.forEach { device ->
//            if (device.isEnabled) {
//                bluetoothManager.sendData(notifications)
//            }
//        }
//    }
}