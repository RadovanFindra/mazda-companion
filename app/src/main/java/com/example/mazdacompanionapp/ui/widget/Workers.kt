package com.example.mazdacompanionapp.ui.widget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mazdacompanionapp.data.AppDataContainer
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull


class ChangeDeviceStateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val appContainer = AppDataContainer(applicationContext)
        val devicesRepository = appContainer.deviceItemsRepository

        val deviceId = inputData.getInt("device_id", -1)
        if (deviceId == -1) return Result.failure()

        val deviceItem = devicesRepository.getDeviceItemStream(deviceId).firstOrNull() ?: return Result.failure()
        val updatedDeviceItem = deviceItem.copy(isEnabled = !deviceItem.isEnabled)
        devicesRepository.updateDeviceItem(updatedDeviceItem)

        // Optionally, refresh the stored data
        val deviceItems = devicesRepository.getAllDeviceItemsStream().firstOrNull() ?: emptyList()
        saveDeviceItemsToPreferences(deviceItems)

        return Result.success()
    }

    private fun saveDeviceItemsToPreferences(deviceItems: List<DeviceItem>) {
        val sharedPreferences = applicationContext.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val deviceItemsJson = Gson().toJson(deviceItems)
        editor.putString("device_items", deviceItemsJson)
        editor.apply()
    }
}