package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.UpdateEvents.Event

interface Sender {
    fun AddToSender(entry: Pair<Event, DeviceItem>)
    fun RemoveFromSender(entry: Pair<Event, DeviceItem>)

    fun Send()

}