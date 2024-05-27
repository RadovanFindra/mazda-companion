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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.ContextCompat

class MyBluetoothManager(private val activity: ComponentActivity, private val bluetoothService: BluetoothService) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private val _discoveredDevices = mutableStateListOf<BluetoothDeviceItem>()
    val discoveredDevices: List<BluetoothDeviceItem> get() = _discoveredDevices

    fun initialize() {
        requestPermissionsLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    Toast.makeText(activity, "Permission ${it.key} denied", Toast.LENGTH_SHORT).show()
                }
            }
            startDiscovery()
        }

        checkPermissions()
        registerReceiver()
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
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            startDiscovery()
        }
    }

    private fun startDiscovery() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        bluetoothAdapter?.takeIf { it.isEnabled }?.apply {
            if (isDiscovering) {
                cancelDiscovery()
            }
            startDiscovery()
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity.registerReceiver(receiver, filter)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action ?: return
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceAddress = device?.address

                    device?.let {
                        val item = BluetoothDeviceItem(name = deviceName, address = deviceAddress ?: "")
                        if (!_discoveredDevices.any { it.address == item.address }) {
                            _discoveredDevices.add(item)
                        }
                    }
                }
            }
        }
    }

    fun connectToDevice(device: BluetoothDeviceItem) {
        bluetoothAdapter?.let { adapter ->
            val remoteDevice = adapter.getRemoteDevice(device.address)
            connectToDevice(remoteDevice)
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            bluetoothService.connectToBluetoothDevice(device.address)
        } else {
            Toast.makeText(activity, "Bluetooth connect permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    fun unregisterReceiver() {
        activity.unregisterReceiver(receiver)
    }
}
