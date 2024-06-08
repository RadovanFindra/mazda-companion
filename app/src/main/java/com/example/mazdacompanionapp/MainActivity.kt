package com.example.mazdacompanionapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.mazdacompanionapp.Sender.BluetoothSender
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
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_CODE
            )
        } else {
            checkAllPermissionsGranted()
        }
    }

    private fun checkAllPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
                Toast.makeText(
                    this,
                    "Please grant notification access and restart App",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                startActivity(intent)
            } else {
                onPermissionsGranted()
            }
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
            val deniedPermissions =
                grantResults.indices.filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
                    .map { permissions[it] }

            if (deniedPermissions.isEmpty()) {
                checkAllPermissionsGranted()
            } else {
                onPermissionsDenied(deniedPermissions)
            }
        }
    }

    private fun onPermissionsGranted() {
        val app = application as CompanionApplication
        val bluetoothSender = BluetoothSender(
            app.container.deviceItemsRepository,
            app.container.eventsRepository,
            app.phoneInfoManager,
            app.bluetoothManager
        )


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
        // Handle denied permissions here
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





