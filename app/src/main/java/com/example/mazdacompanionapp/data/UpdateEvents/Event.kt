
package com.example.mazdacompanionapp.data.UpdateEvents

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.AppInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val preset: SEND_EVENT_PRESET,
    var isEnabled: Boolean,
    val selectedApps: List<AppInfo>
)

class Converters {
    private val gson = Gson()

    // Converters for SEND_EVENT_PRESET
    @TypeConverter
    fun fromSendEventPreset(preset: SEND_EVENT_PRESET): String {
        return preset.name
    }

    @TypeConverter
    fun toSendEventPreset(value: String): SEND_EVENT_PRESET {
        return SEND_EVENT_PRESET.valueOf(value)
    }

    // Converters for List<AppInfo>
    @TypeConverter
    fun fromAppInfoList(list: List<AppInfo>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toAppInfoList(value: String?): List<AppInfo> {
        if (value.isNullOrEmpty()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<AppInfo>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}

enum class SEND_EVENT_PRESET(val title: String) {
    DEFAULT("Default"),
    MUSIC_UPDATE("Music Update"),
    ON_CONNECT("On Connect"),
    ON_NOTIFICATION_CHANCE("On Notification Change")
}

