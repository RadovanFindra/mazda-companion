package com.example.mazdacompanionapp

import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
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

    init {
        CoroutineScope(Dispatchers.IO).launch {
            deviceItemsRepository.getAllDeviceItemsStream().collect { deviceItems ->
                _devices.value = deviceItems
            }
        }
    }

    suspend fun sendToDevices() {
        val notifications = NotificationListener.notificationsToJson()

        devices.value.forEach { device ->
            if (device.isEnabled) {
                bluetoothManager.connectToDevice(device.address)
                bluetoothManager.sendData(notifications)
            }
        }
    }
}