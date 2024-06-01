
package com.example.mazdacompanionapp.data.BluetoothDevices

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [DeviceItem] from a given data source.
 */
interface DeviceItemsRepository {
    /**
     * Retrieve all the deviceItems from the the given data source.
     */
    fun getAllDeviceItemsStream(): Flow<List<DeviceItem>>

    /**
     * Retrieve an deviceItem from the given data source that matches with the [id].
     */
    fun getDeviceItemStream(id: Int): Flow<DeviceItem?>

    /**
     * Insert deviceItem in the data source
     */
    suspend fun insertDeviceItem(deviceItem: DeviceItem)

    /**
     * Delete deviceItem from the data source
     */
    suspend fun deleteDeviceItem(deviceItem: DeviceItem)

    /**
     * Update deviceItem in the data source
     */
    suspend fun updateDeviceItem(deviceItem: DeviceItem)
}
