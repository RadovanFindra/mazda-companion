
package com.example.mazdacompanionapp.data.BluetoothDevices

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface DeviceItemDao {
    @Query("SELECT * from devices ORDER BY name ASC")
    fun getAllDeviceItems(): Flow<List<DeviceItem>>

    @Query("SELECT * from devices WHERE id = :id")
    fun getDeviceItem(id: Int): Flow<DeviceItem>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing DeviceItem into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(deviceItem: DeviceItem)

    @Update
    suspend fun update(deviceItem: DeviceItem)

    @Delete
    suspend fun delete(deviceItem: DeviceItem)
}
