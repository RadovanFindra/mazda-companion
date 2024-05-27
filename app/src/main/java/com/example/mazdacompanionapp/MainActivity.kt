package com.example.mazdacompanionapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

val _discoveredDevices = mutableStateListOf<BluetoothDeviceItem>()
val discoveredDevices: List<BluetoothDeviceItem> get() = _discoveredDevices
class MainActivity : ComponentActivity() {

    private lateinit var selectedDevice: BluetoothDeviceItem
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>


    private val bluetoothService = BluetoothService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    Toast.makeText(this, "Permission ${it.key} denied", Toast.LENGTH_SHORT).show()
                }
            }
            startDiscovery()
        }

        checkPermissions()

        // Register for broadcasts when a device is discovered
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        setContent {
            MazdaCompanionAppTheme {
                Surface {
                    MainScreen(
                        onNotificationButtonClick = { sendNotificationData() },
                        onDeviceClick = { onDeviceSelected(it) }
                    )
                }
            }
        }
    }

    private fun checkPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            startDiscovery()
        }
    }
    private fun startDiscovery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        bluetoothAdapter?.takeIf { it.isEnabled }?.apply {
            if (isDiscovering) {
                cancelDiscovery()
            }
            startDiscovery()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action ?: return

            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceAddress = device?.address // MAC address

                    device?.let {
                        val item = BluetoothDeviceItem(name = deviceName, address = deviceAddress ?: "")
                        _discoveredDevices.add(item)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun onDeviceSelected(device: BluetoothDeviceItem) {
        selectedDevice = device
        bluetoothAdapter?.let { adapter ->
            val remoteDevice = adapter.getRemoteDevice(device.address)
            // Initiate connection with the selected device (implement your connection logic)
            connectToDevice(remoteDevice)
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        // Implement your Bluetooth connection logic here
        Toast.makeText(this, "Connecting to ${device.name}", Toast.LENGTH_SHORT).show()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            bluetoothService.connectToBluetoothDevice(device.address)

        } else {
            Toast.makeText(this, "Bluetooth connect permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotificationData() {
        val json = NotificationListener.notificationsToJson()
        bluetoothService.sendData(json)
    }

}

@Composable
fun MainScreen(onNotificationButtonClick: () -> Unit, onDeviceClick: (BluetoothDeviceItem) -> Unit) {
    var selectedDevice by remember { mutableStateOf<BluetoothDeviceItem?>(null) }
    if (selectedDevice == null) {
        DeviceList(onDeviceSelected = { selectedDevice = it })
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
fun DeviceList(onDeviceSelected: (BluetoothDeviceItem) -> Unit) {
    val context = LocalContext.current

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
    val notifications = NotificationListener.notificationsLiveData.observeAsState(emptyList())
    LazyColumn {
        items(notifications.value) { notification ->
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


