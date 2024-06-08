package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem

interface Sender {
    fun AddToSender(deviceItem: DeviceItem)
    fun RemoveFromSender(deviceItem: DeviceItem)

    fun Send()

}