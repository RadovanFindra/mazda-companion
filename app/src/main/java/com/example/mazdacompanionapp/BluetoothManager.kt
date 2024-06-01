package com.example.mazdacompanionapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.mazdacompanionapp.screens.Bluetooth.BluetoothDeviceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class MyBluetoothManager(private val context: Context, private val bluetoothService: BluetoothService) {

    private val _discoveredDevicesFlow = MutableStateFlow<List<BluetoothDeviceItem>>(emptyList())
    val discoveredDevicesFlow: StateFlow<List<BluetoothDeviceItem>> get() = _discoveredDevicesFlow

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private val deviceLastSeenMap = ConcurrentHashMap<String, Long>()
    private var cleanupJob: Job? = null

    fun initialize() {
        checkPermissions()
        registerReceiver()
        startDeviceCleanup()
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
        cleanupJob?.cancel()
    }

    fun startDiscovery() {
        bluetoothAdapter?.startDiscovery()
    }

    fun cancelDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }

    fun connectToDevice(device: BluetoothDeviceItem) {
        bluetoothService.connectToBluetoothDevice(device.address)
    }

    fun getDiscoveredDevicesFlow(): Flow<List<BluetoothDeviceItem>> {
        return discoveredDevicesFlow
    }

    private fun startDeviceCleanup() {
        cleanupJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val currentTime = System.currentTimeMillis()
                val updatedList = _discoveredDevicesFlow.value.filter {
                    val lastSeen = deviceLastSeenMap[it.address] ?: currentTime
                    currentTime - lastSeen <= DEVICE_TIMEOUT
                }
                _discoveredDevicesFlow.value = updatedList
                delay(CLEANUP_INTERVAL)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action ?: return
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    val deviceItem = BluetoothDeviceItem(it.name ?: "Unknown Device", it.address)
                    deviceLastSeenMap[it.address] = System.currentTimeMillis()
                    val currentList = _discoveredDevicesFlow.value.toMutableList()
                    if (!currentList.contains(deviceItem)) {
                        currentList.add(deviceItem)
                        _discoveredDevicesFlow.value = currentList
                    }
                }
            }
        }
    }

    companion object {
        private const val DEVICE_TIMEOUT = 60000L // 1 minute
        private const val CLEANUP_INTERVAL = 10000L // 10 seconds
    }
}

