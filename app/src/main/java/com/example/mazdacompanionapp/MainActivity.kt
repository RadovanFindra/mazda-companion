package com.example.mazdacompanionapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    private val permissions = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkAndRequestPermissions() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE)
        } else {
            onPermissionsGranted()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            val deniedPermissions = grantResults.indices.filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
                .map { permissions[it] }

            if (deniedPermissions.isEmpty()) {
                onPermissionsGranted()
            } else {
                onPermissionsDenied(deniedPermissions)
            }
        }
    }

    private fun onPermissionsGranted() {
        setContent {
            MazdaCompanionAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    CompanionApp()
                }
            }
        }
    }

    private fun onPermissionsDenied(deniedPermissions: List<String>) {

    }

    companion object {
        private const val REQUEST_CODE = 523
    }
}

//    private fun sendNotificationData() {
//        val json = NotificationListener.notificationsToJson()
//        bluetoothService.sendData(json)
//    }
//
//    private fun startPeriodicDataSend() {
//        CoroutineScope(Dispatchers.IO).launch {
//            while (true) {
//                delay(60000) // 1 minute
//                val data = JSONObject().put("preset", "default").put("timestamp", System.currentTimeMillis())
//                bluetoothService.sendData(data)
//            }
//        }
//    }




