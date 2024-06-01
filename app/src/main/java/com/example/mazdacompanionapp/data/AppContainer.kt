
package com.example.mazdacompanionapp.data

import android.content.Context
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsDatabase
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.BluetoothDevices.OfflineDeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.EventsDatabase
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.OfflineEventsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val eventsRepository: EventsRepository
    val deviceItemsRepository: DeviceItemsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val eventsRepository: EventsRepository by lazy {
        OfflineEventsRepository(EventsDatabase.getDatabase(context).eventDao())
    }

    override val deviceItemsRepository: DeviceItemsRepository by lazy {
        OfflineDeviceItemsRepository(DeviceItemsDatabase.getDatabase(context).deviceItemDao())
    }
}
