package com.example.mazdacompanionapp.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.Switch
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CompanionWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        scheduleDataFetch(context)

        val deviceItemsList = getDeviceItemsFromPreferences(context)

        provideContent {
            WidgetBody(
                deviceItemsList = deviceItemsList,
                onDeviceSwitch = { deviceId -> changeEnableState(context, deviceId) }
            )
        }
    }

    private fun scheduleDataFetch(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<FetchDeviceItemsWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun getDeviceItemsFromPreferences(context: Context): List<DeviceItem> {
        val sharedPreferences = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val deviceItemsJson = sharedPreferences.getString("device_items", "[]")
        return Gson().fromJson(deviceItemsJson, object : TypeToken<List<DeviceItem>>() {}.type)
    }

    private fun changeEnableState(context: Context, deviceId: Int) {
        val workRequest = OneTimeWorkRequestBuilder<ChangeDeviceStateWorker>()
            .setInputData(workDataOf("device_id" to deviceId))
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

@Composable
fun WidgetBody(
    deviceItemsList: List<DeviceItem>,
    onDeviceSwitch: (Int) -> Unit,
) {
    GlanceTheme {
        Column {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.surface)
                    .cornerRadius(8.dp),
            ) {
                DeviceItemsListWidget(
                    deviceItemsList = deviceItemsList,
                    modifier = GlanceModifier.padding(8.dp),
                    onDeviceSwitch = onDeviceSwitch
                )
            }
        }
    }
}

@Composable
fun DeviceItemsListWidget(
    deviceItemsList: List<DeviceItem>,
    modifier: GlanceModifier,
    onDeviceSwitch: (Int) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(deviceItemsList) { item ->
            BluetoothItemWidget(
                item = item,
                onDeviceSwitch = onDeviceSwitch
            )
        }
    }
}

@Composable
fun BluetoothItemWidget(
    item: DeviceItem,
    onDeviceSwitch: (Int) -> Unit,
) {
    Row {
        item.name?.let { Text(text = it, style = TextStyle(color = GlanceTheme.colors.onSurface)) }
        Switch(
            checked = item.isEnabled,
            onCheckedChange = { onDeviceSwitch(item.id) }
        )
    }
}