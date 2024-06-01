package com.example.mazdacompanionapp.screens.Bluetooth

import androidx.lifecycle.ViewModel
import com.example.mazdacompanionapp.MyBluetoothManager
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository

class DeviceItemAddViewModel(
    private val deviceItemsRepository: DeviceItemsRepository,
    bluetoothManager: MyBluetoothManager): ViewModel() {

    var bluetoothManager: MyBluetoothManager = bluetoothManager
        private set


    suspend fun saveDevice(bluetoothDeviceItem: BluetoothDeviceItem) {
        if (validateInput(bluetoothDeviceItem)) {
            deviceItemsRepository.insertDeviceItem(bluetoothDeviceItem.toDeviceItem())
            bluetoothManager.cancelDiscovery()
        }
    }

    private fun validateInput(bluetoothDeviceItem: BluetoothDeviceItem): Boolean{
        return bluetoothDeviceItem.address.isNotBlank()

    }
}


data class BluetoothDeviceItem(
    val name: String? ="",
    val address: String = ""
)


fun BluetoothDeviceItem.toDeviceItem(): DeviceItem {
    return DeviceItem(
        name = name,
        address = address,
        events = mutableListOf(),
        isEnabled = false
    )
}