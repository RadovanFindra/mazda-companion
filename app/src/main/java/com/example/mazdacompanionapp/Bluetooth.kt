package com.example.mazdacompanionapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import org.json.JSONObject
import java.io.OutputStream
import java.util.UUID

data class BluetoothDeviceItem(
    val name: String?,
    val address: String
)

class BluetoothService {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var outputStream: OutputStream
    private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun connectToBluetoothDevice(deviceAddress: String) {
        val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(deviceAddress) ?: return

        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID)
            bluetoothSocket.connect()
            outputStream = bluetoothSocket.outputStream
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendData(data: JSONObject) {
        try {
            outputStream.write(data.toString().toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}