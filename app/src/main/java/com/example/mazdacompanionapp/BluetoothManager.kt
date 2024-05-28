package com.example.mazdacompanionapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.mutableStateListOf

class MyBluetoothManager(private val context: Context, private val bluetoothService: BluetoothService) {

    private val _discoveredDevices = mutableStateListOf<BluetoothDeviceItem>()
    val discoveredDevices: List<BluetoothDeviceItem> get() = _discoveredDevices

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun initialize() {
        checkPermissions()
        registerReceiver()
    }

    private fun checkPermissions() {
        // Implement permission checks
    }

    private fun registerReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(receiver)
    }

    fun startDiscovery() {
        bluetoothAdapter?.startDiscovery()
    }

    fun connectToDevice(device: BluetoothDeviceItem) {
        bluetoothService.connectToBluetoothDevice(device.address)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action ?: return
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                _discoveredDevices.add(BluetoothDeviceItem(device.name ?: "Unknown", device.address))
            }
        }
    }
}
