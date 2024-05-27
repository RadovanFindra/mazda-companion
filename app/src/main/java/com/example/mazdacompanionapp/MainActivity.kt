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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme
import org.json.JSONObject
import java.util.UUID


class MainActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    private val _discoveredDevices = mutableStateListOf<BluetoothDeviceItem>()
    private val discoveredDevices: List<BluetoothDeviceItem> get() = _discoveredDevices

    private lateinit var bluetoothService: BluetoothService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothService = BluetoothService(this)

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
                Surface(color = MaterialTheme.colors.background) {
                    DeviceList(discoveredDevices) { device ->
                        onDeviceSelected(device)
                    }
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
        bluetoothAdapter?.let { adapter ->
            val remoteDevice = adapter.getRemoteDevice(device.address)
            // Initiate connection with the selected device (implement your connection logic)
            connectToDevice(remoteDevice)
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        // Implement your Bluetooth connection logic here
        Toast.makeText(this, "Connecting to ${device.name}", Toast.LENGTH_SHORT).show()
        val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            bluetoothService.startClient(device, MY_UUID)
            sendNotificationData()
        } else {
            Toast.makeText(this, "Bluetooth connect permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotificationData() {
        val json = JSONObject().apply {
            put("appName", "ExampleApp")
            put("title", "New Notification")
            put("text", "This is a notification text")
            put("timestamp", System.currentTimeMillis().toString())
        }

        bluetoothService.sendData(json)
    }



}

@Composable
fun DeviceList(devices: List<BluetoothDeviceItem>, onDeviceClick: (BluetoothDeviceItem) -> Unit) {
    LazyColumn {
        items(devices) { device ->
            DeviceItem(device = device, onClick = { onDeviceClick(device) })
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
