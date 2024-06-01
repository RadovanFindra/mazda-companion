
package com.example.mazdacompanionapp.data.UpdateEvents

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val preset: Preset,
    var isEnabled: Boolean
)

enum class Preset(val title: String) {
    DEFAULT("Default"),
    MUSIC_MESSAGE("Music Message"),
    EVENTS("Events")
}

