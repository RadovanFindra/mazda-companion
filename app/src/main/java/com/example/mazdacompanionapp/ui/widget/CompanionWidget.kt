package com.example.mazdacompanionapp.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.glance.layout.absolutePadding
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.mazdacompanionapp.data.AppDataContainer
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
/**
 *Widget
 */
class CompanionWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetBody(
                context = context,
                onDeviceSwitch = { deviceId -> changeEnableState(context, deviceId) }
            )
        }
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
    context: Context,
    onDeviceSwitch: (Int) -> Unit,
) {
    val appContainer = AppDataContainer(context.applicationContext)
    val devicesRepository = appContainer.deviceItemsRepository
    val deviceItemsFlow by rememberUpdatedState(newValue = devicesRepository.getAllDeviceItemsStream())

    val deviceItemsList by deviceItemsFlow.collectAsState(initial = emptyList())

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
        item.name?.let {
            Text(
                text = it,
                style = TextStyle(color = GlanceTheme.colors.onSurface)
            )
        }
        Switch(
            checked = item.isEnabled,
            onCheckedChange = { onDeviceSwitch(item.id) },
            modifier = GlanceModifier
                .fillMaxWidth()
                .absolutePadding(right = 2.dp)
        )
    }
}