package com.example.mazdacompanionapp.Sender

import com.example.mazdacompanionapp.Bluetooth.MyBluetoothManager
import com.example.mazdacompanionapp.Notification.NotificationListener
import com.example.mazdacompanionapp.PhoneInfoManager
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.CopyOnWriteArrayList

class PeriodicalSender(
    private val bluetoothManager: MyBluetoothManager,
    private val phoneInfoManager: PhoneInfoManager
) : Sender {
    private val enties: MutableList<DeviceItem> = CopyOnWriteArrayList()

    override fun AddToSender(deviceItem: DeviceItem) {
        val index = enties.indexOfFirst { it.id == deviceItem.id }
        if (index != -1) {
            enties[index] = deviceItem
        } else {
            enties.add(deviceItem)
        }
    }

    override fun RemoveFromSender(deviceItem: DeviceItem) {
        enties.removeIf { it.id == deviceItem.id }
    }

    init {
        Send()
    }

    override fun Send() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val notificationsJson = JSONArray()
                for (entry in enties) {
                    if (entry.isEnabled) {
                        if (!bluetoothManager.isDeviceConnected(entry.address)) {
                            bluetoothManager.connectToDevice(entry.address)
                        }
                        val notifications = NotificationListener.notificationsLiveData.value

                        for (event in entry.events) {
                            if (event.isEnabled) {
                                val toHanldle =
                                    event.selectedApps.find { it.name == "PhoneInfo" }
                                if (toHanldle != null) {
                                    notificationsJson.put(HandleSpecial(toHanldle))
                                }
                                if (notifications != null) {
                                    for (notificationData in notifications) {

                                        if (event.selectedApps.find { appInfo -> appInfo.name == notificationData.appName } != null && event.isEnabled) {
                                            val jsonObject = JSONObject().apply {
                                                put("appName", notificationData.appName)
                                                put("title", notificationData.title)
                                                put("text", notificationData.text)
                                            }
                                            notificationsJson.put(jsonObject)
                                        }
                                    }

                                }
                            }

                        }
                        val sendJson: JSONObject =
                            JSONObject().apply { put("notifications", notificationsJson) }
                        bluetoothManager.sendData(entry.address, sendJson)
                    }
                }
                delay(1500)
            }
        }
    }

    override fun HandleSpecial(appInfo: AppInfo): JSONObject {
        return when (appInfo.name) {
            "PhoneInfo" -> phoneInfoManager.createPhoneInfoJson()
            else -> {
                JSONObject()
            }
        }
    }
}