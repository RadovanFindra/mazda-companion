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
import java.util.concurrent.CopyOnWriteArrayList

class PeriodicalSender(private val bluetoothManager: MyBluetoothManager) : Sender {
    private val enties: MutableList<Pair<Event, DeviceItem>> = CopyOnWriteArrayList()

    override fun AddToSender(entry: Pair<Event, DeviceItem>) {
        val (event, deviceItem) = entry
        val exists = enties.any { it.first.id == event.id && it.second.id == deviceItem.id }
        if (!exists) {
            enties.add(entry)
        }
    }

    override fun RemoveFromSender(entry: Pair<Event, DeviceItem>) {
        val (event, deviceItem) = entry
        val entryToRemove = enties.find { it.first.id == event.id && it.second.id == deviceItem.id }
        entryToRemove?.let {
            enties.remove(it)
        }
    }
    init {
        Send()
    }
    override fun Send() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                println(enties.size)

                enties.forEach { entry ->
                    if (entry.second.isEnabled) {
                        if (!bluetoothManager.isDeviceConnected(entry.second.address)) {
                            bluetoothManager.connectToDevice(entry.second.address)
                        }
                        val notifications = NotificationListener.notificationsLiveData
                        val notificationsJson = JSONArray()

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
                        bluetoothManager.sendData(entry.second.address, JSONObject().apply {
                            put(
                                "notifications",
                                notificationsJson
                            )
                        })
                    }

                }
                delay(600)
            }
        }
    }
}