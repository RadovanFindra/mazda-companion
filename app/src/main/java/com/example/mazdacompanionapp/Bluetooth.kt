package com.example.mazdacompanionapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

data class BluetoothDeviceItem(
    val name: String?,
    val address: String
)

class BluetoothService(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    fun startClient(device: BluetoothDevice, uuid: UUID) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothService", "Bluetooth connect permission not granted")
            return
        }
        connectThread = ConnectThread(device, uuid)
        connectThread?.start()
    }

    fun sendData(data: JSONObject) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothService", "Bluetooth connect permission not granted")
            return
        }
        connectedThread?.write(data.toString().toByteArray())
    }

    private inner class ConnectThread(device: BluetoothDevice, private val uuid: UUID) : Thread() {
        private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                device.createRfcommSocketToServiceRecord(uuid)
            } else {
                null
            }
        }

        override fun run() {
            bluetoothAdapter?.cancelDiscovery()
            socket?.use { socket ->
                try {
                    socket.connect()
                    connectedThread = ConnectedThread(socket)
                    connectedThread?.start()
                } catch (e: IOException) {
                    Log.e("BluetoothService", "Unable to connect", e)
                    try {
                        socket.close()
                    } catch (closeException: IOException) {
                        Log.e("BluetoothService", "Could not close the client socket", closeException)
                    }
                    return
                }
            }
        }
    }

    private inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {
        private val inputStream: InputStream = socket.inputStream
        private val outputStream: OutputStream = socket.outputStream

        override fun run() {
            val buffer = ByteArray(1024)
            var numBytes: Int
            while (true) {
                numBytes = try {
                    inputStream.read(buffer)
                } catch (e: IOException) {
                    Log.e("BluetoothService", "Input stream was disconnected", e)
                    break
                }
            }
        }

        fun write(bytes: ByteArray) {
            try {
                outputStream.write(bytes)
            } catch (e: IOException) {
                Log.e("BluetoothService", "Error occurred when sending data", e)
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                Log.e("BluetoothService", "Could not close the connect socket", e)
            }
        }
    }
}
