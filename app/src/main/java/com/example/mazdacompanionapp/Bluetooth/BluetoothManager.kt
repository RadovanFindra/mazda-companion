package com.example.mazdacompanionapp.Bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.BluetoothDeviceItem
import org.json.JSONObject

class MyBluetoothManager(
    private val context: Context,
    private val bluetoothService: BluetoothService
) {

    private val _discoveredDevices = mutableStateListOf<BluetoothDeviceItem>()
    val discoveredDevices: List<BluetoothDeviceItem> get() = _discoveredDevices

    private var isDiscovering: Boolean = false

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun initialize() {
        registerReceiver()
    }

    private fun registerReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(receiver)
    }
    fun discovering():Boolean{
        return isDiscovering
    }

    fun startDiscovery() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter?.startDiscovery()
            isDiscovering = true
        }
    }

    fun cancelDiscovery() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter?.cancelDiscovery()
            isDiscovering = false
            _discoveredDevices.clear()
        }
    }

    fun connectToDevice(address: String) {
        bluetoothService.connectToBluetoothDevice(address)
    }

    fun sendData(data: JSONObject) {
        bluetoothService.sendDataToAll(data)
    }

    fun sendData(deviceAddress: String ,data: JSONObject) {
        bluetoothService.sendDataToDevice(deviceAddress, data)
    }
    fun isDeviceConnected(deviceAddress: String): Boolean {
        return bluetoothService.isDeviceConnected(deviceAddress)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceItem =
                        device?.let {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            BluetoothDeviceItem(
                                device.name ?: "Unknown Device",
                                it.address
                            )
                        }
                    if (!_discoveredDevices.contains(deviceItem)) {
                        deviceItem?.let { _discoveredDevices.add(it) }
                    }
                }
            }
        }
    }
}