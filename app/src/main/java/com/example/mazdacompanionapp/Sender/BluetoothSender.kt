package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.Bluetooth.MyBluetoothManager
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.SEND_EVENT_PRESET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BluetoothSender(
    private val deviceItemsRepository: DeviceItemsRepository,
    private val bluetoothManager: MyBluetoothManager
) {
    private val _devices = MutableStateFlow<List<DeviceItem>>(emptyList())
    val devices = _devices.asStateFlow()
    private val periodicSender: Sender = PeriodicalSender(bluetoothManager)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            deviceItemsRepository.getAllDeviceItemsStream().collect { deviceItems ->
                _devices.value = deviceItems
                startSending() // Call startSending whenever device database changes
            }
        }
    }

    private fun startSending() {
        var sender: Sender = periodicSender
        for (deviceItem in devices.value) {
            for (event in deviceItem.events) {
                when (event.preset) {
                    SEND_EVENT_PRESET.DEFAULT -> sender = periodicSender
                    SEND_EVENT_PRESET.ON_CONNECT -> println()
                    SEND_EVENT_PRESET.ON_NOTIFICATION_CHANCE -> println()
                    else -> {
                    }
                }
                val temp = Pair(event, deviceItem)
                if (deviceItem.isEnabled && event.isEnabled) {
                    sender.AddToSender(temp)
                } else {
                    sender.RemoveFromSender(temp)
                }
            }
        }
    }
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
