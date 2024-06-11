package com.example.mazdacompanionapp

import android.app.Application
import com.example.mazdacompanionapp.bluetooth.BluetoothService
import com.example.mazdacompanionapp.bluetooth.MyBluetoothManager
import com.example.mazdacompanionapp.data.AppDataContainer
import com.example.mazdacompanionapp.getters.InstalledAppsGetter
import com.example.mazdacompanionapp.phoneInfo.PhoneInfoManager

/**
 *Application instances
 */
class CompanionApplication : Application() {
    lateinit var container: AppDataContainer
    lateinit var bluetoothService: BluetoothService
    lateinit var bluetoothManager: MyBluetoothManager
    lateinit var phoneInfoManager: PhoneInfoManager
    lateinit var installedAppsGetter: InstalledAppsGetter

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        bluetoothService = BluetoothService(this)
        bluetoothManager = MyBluetoothManager(this, bluetoothService)
        bluetoothManager.initialize()
        phoneInfoManager = PhoneInfoManager(this)
        installedAppsGetter = InstalledAppsGetter(this)
    }

}