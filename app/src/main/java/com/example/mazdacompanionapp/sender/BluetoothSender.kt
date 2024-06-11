package com.example.mazdacompanionapp.sender

import com.example.mazdacompanionapp.bluetooth.MyBluetoothManager
import com.example.mazdacompanionapp.phoneInfo.PhoneInfoManager
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.SEND_EVENT_PRESET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
/**
 * Create Sender based on Presets
 */
class BluetoothSender(
    private val deviceItemsRepository: DeviceItemsRepository,
    eventsRepository: EventsRepository,
    phoneInfoManager: PhoneInfoManager,
    bluetoothManager: MyBluetoothManager
) {
    private val _devices = MutableStateFlow<List<DeviceItem>>(emptyList())
    val devices = _devices.asStateFlow()
    private val periodicSender: Sender = PeriodicalSender(bluetoothManager, phoneInfoManager)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            combine(
                deviceItemsRepository.getAllDeviceItemsStream(),
                eventsRepository.getAllEventsStream()
            ) { deviceItems, events ->
                Pair(deviceItems, events)
            }.collect { (deviceItems) ->
                _devices.value = deviceItems
                startSending()
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


            }
            if (deviceItem.isEnabled) {
                sender.AddToSender(deviceItem)
            } else {
                sender.RemoveFromSender(deviceItem)
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
