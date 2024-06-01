package com.example.mazdacompanionapp.data.BluetoothDevices

import androidx.room.PrimaryKey
import com.example.mazdacompanionapp.data.UpdateEvents.Event

/**
 * Entity data class represents a single row in the database.
 */
@androidx.room.Entity(tableName = "devices")
data class DeviceItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val address: String,
    val events: MutableList<Event>,
    val isEnabled: Boolean
)

