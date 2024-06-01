package com.example.mazdacompanionapp

import android.app.Application
import com.example.mazdacompanionapp.data.AppDataContainer

class CompanionApplication : Application() {
    lateinit var container: AppDataContainer
    lateinit var bluetoothService: BluetoothService
    lateinit var bluetoothManager: MyBluetoothManager

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        bluetoothService = BluetoothService()
        bluetoothManager = MyBluetoothManager(this, bluetoothService)
        bluetoothManager.initialize()

    }

}