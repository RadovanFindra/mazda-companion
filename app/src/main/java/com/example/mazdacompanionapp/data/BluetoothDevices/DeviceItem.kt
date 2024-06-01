package com.example.mazdacompanionapp.data.BluetoothDevices

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.Preset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "devices")
data class DeviceItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val address: String,
    val events: MutableList<Event>,
    val isEnabled: Boolean
)


class Converters {
    @TypeConverter
    fun fromEventList(value: MutableList<Event>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toEventList(value: String): MutableList<Event> {
        val listType = object : TypeToken<MutableList<Event>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPreset(value: Preset): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPreset(value: String): Preset {
        return Gson().fromJson(value, Preset::class.java)
    }
}

