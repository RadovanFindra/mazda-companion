package com.example.mazdacompanionapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.mazdacompanionapp.Notification.NotificationData
import com.example.mazdacompanionapp.Notification.NotificationListener
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.BluetoothDeviceItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(
    onNotificationButtonClick: () -> Unit,
    onDeviceClick: (BluetoothDeviceItem) -> Unit,
    discoveredDevices: List<BluetoothDeviceItem>
) {
    var selectedDevice by remember { mutableStateOf<BluetoothDeviceItem?>(null) }
    if (selectedDevice == null) {
        DeviceList(onDeviceSelected = { selectedDevice = it }, discoveredDevices = discoveredDevices)
    } else {
        onDeviceClick(selectedDevice!!)
        Column {
            Button(onClick = { selectedDevice = null }) {
                Text(text = "Back to Device List")
            }
            Button(onClick = { onNotificationButtonClick() }) {
                Text(text = "Send Notification Data")
            }
            NotificationList()
        }
    }
}

@Composable
fun DeviceList(onDeviceSelected: (BluetoothDeviceItem) -> Unit, discoveredDevices: List<BluetoothDeviceItem>) {
    LazyColumn {
        items(discoveredDevices) { device ->
            DeviceItem(device = device, onClick = { onDeviceSelected(device) })
        }
    }
}

@Composable
fun DeviceItem(device: BluetoothDeviceItem, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .clickable { onClick() }
    ) {
        Text(text = device.name ?: "Unknown Device", style = MaterialTheme.typography.h6)
        Text(text = device.address, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun NotificationList() {
    val notifications by NotificationListener.notificationsLiveData.observeAsState(emptyList())
    LazyColumn {
        items(notifications) { notification ->
            NotificationItem(notification)
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationData) {
    Column(modifier = Modifier.padding(16.dp)) {
        notification.appName?.let { Text(text = "App: $it", style = MaterialTheme.typography.h6) }
        notification.title?.let { Text(text = "Title: $it", style = MaterialTheme.typography.h6) }
        notification.text?.let { Text(text = "Text: $it", style = MaterialTheme.typography.body1) }
        Text(text = "Time: ${formatTimestamp(notification.timestamp)}", style = MaterialTheme.typography.caption)
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
