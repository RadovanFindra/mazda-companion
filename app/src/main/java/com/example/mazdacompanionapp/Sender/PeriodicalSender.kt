package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.Bluetooth.MyBluetoothManager
import com.example.mazdacompanionapp.Notification.NotificationListener
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class PeriodicalSender(private val bluetoothManager: MyBluetoothManager) : Sender {
    val enties: MutableList<Pair<Event, DeviceItem>> = mutableListOf()

    override fun AddToSender(event: Event, deviceItem: DeviceItem) {
        enties.add(Pair(event, deviceItem))
    }

    override fun RemoveFromSender(event: Event, deviceItem: DeviceItem) {
        enties.remove(Pair(event, deviceItem))
    }

    override fun StartSender() {
        CoroutineScope(Dispatchers.IO).launch {
            enties.forEach { entry ->
                bluetoothManager.connectToDevice(entry.second.address)
            }
            while (enties.isNotEmpty()) {
                val notifications = NotificationListener.notificationsLiveData
                val notificationsJson = JSONArray()
                enties.forEach { entry ->
                    notifications.value?.forEach { notificationData ->
                        if (entry.first.selectedApps.find { appInfo -> appInfo.name == notificationData.appName } != null) {
                            val jsonObject = JSONObject().apply {
                                put("appName", notificationData.appName)
                                put("title", notificationData.title)
                                put("text", notificationData.text)
                            }
                            notificationsJson.put(jsonObject)
                        }
                    }
                }
                bluetoothManager.sendData(JSONObject().apply { put("notifications", notificationsJson) })
                delay(6000)

            }
        }
    }
}