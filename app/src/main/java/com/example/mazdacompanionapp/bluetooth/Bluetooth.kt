package com.example.mazdacompanionapp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import org.json.JSONObject
import java.io.OutputStream
import java.util.UUID


/**
 * Bluetooth Interface for Stream comunication
 */
class BluetoothService(
    private val context: Context,
) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothDevices = mutableListOf<BluetoothDevice>()
    private val bluetoothSockets = mutableListOf<BluetoothSocket>()
    private val outputStreams = mutableListOf<OutputStream>()
    private val connectionStatus = mutableMapOf<String, Boolean>()
    private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun connectToBluetoothDevice(deviceAddress: String) {
        val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(deviceAddress) ?: return

        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID)
                bluetoothSocket.connect()
                val outputStream = bluetoothSocket.outputStream

                bluetoothDevices.add(bluetoothDevice)
                bluetoothSockets.add(bluetoothSocket)
                outputStreams.add(outputStream)
                connectionStatus[deviceAddress] = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            connectionStatus[deviceAddress] = false
        }
    }

    fun isDeviceConnected(deviceAddress: String): Boolean {
        return connectionStatus[deviceAddress] ?: false
    }

    fun getConnectedDevices(): List<String> {
        return bluetoothDevices.map { it.address }
    }

    fun sendDataToAll(data: JSONObject) {
        try {
            val dataBytes = data.toString().toByteArray()
            for (outputStream in outputStreams) {
                outputStream.write(dataBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendDataToDevice(deviceAddress: String, data: JSONObject) {
        val index = bluetoothDevices.indexOfFirst { it.address == deviceAddress }
        if (index != -1) {
            try {
                val dataBytes = (data.toString() + "\n").toByteArray()
                outputStreams[index].write(dataBytes)
            } catch (e: java.io.IOException) {
                e.printStackTrace()
                println("Broken pipe error occurred. Updating connection status.")
                connectionStatus[deviceAddress] = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            println("Device with address $deviceAddress not found.")
        }
    }
}