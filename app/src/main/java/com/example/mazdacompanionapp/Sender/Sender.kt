package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.AppInfo
import org.json.JSONObject

interface Sender {
    fun AddToSender(deviceItem: DeviceItem)
    fun RemoveFromSender(deviceItem: DeviceItem)
    fun Send()
    fun HandleSpecial(appInfo: AppInfo):JSONObject

}