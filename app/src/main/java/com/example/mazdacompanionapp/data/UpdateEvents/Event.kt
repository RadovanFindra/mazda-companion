
package com.example.mazdacompanionapp.data.UpdateEvents

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

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
    val selectedApps: List<String> // Add this parameter to store the package names
)

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}

enum class SEND_EVENT_PRESET(val title: String) {
    DEFAULT("Default"),
    MUSIC_UPDATE("Music Update"),
    ON_CONNECT("On Connect"),
    ON_NOTIFICATION_CHANCE("On Notification Change")
}

