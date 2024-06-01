
package com.example.mazdacompanionapp.data.BluetoothDevices

import kotlinx.coroutines.flow.Flow

class OfflineDeviceItemsRepository(private val deviceItemDao: DeviceItemDao) : DeviceItemsRepository {
    override fun getAllDeviceItemsStream(): Flow<List<DeviceItem>> = deviceItemDao.getAllDeviceItems()

    override fun getDeviceItemStream(id: Int): Flow<DeviceItem?> = deviceItemDao.getDeviceItem(id)

    override suspend fun insertDeviceItem(deviceItem: DeviceItem) = deviceItemDao.insert(deviceItem)

    override suspend fun deleteDeviceItem(deviceItem: DeviceItem) = deviceItemDao.delete(deviceItem)

    override suspend fun updateDeviceItem(deviceItem: DeviceItem) = deviceItemDao.update(deviceItem)
}
