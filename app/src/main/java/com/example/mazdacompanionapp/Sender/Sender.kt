package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.UpdateEvents.Event

interface Sender {
    fun AddToSender(event: Event,deviceItem: DeviceItem)
    fun RemoveFromSender(event: Event,deviceItem: DeviceItem)

    fun StartSender()

}