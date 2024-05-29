
package com.example.mazdacompanionapp.data

import androidx.room.PrimaryKey
import com.example.mazdacompanionapp.screens.Preset

/**
 * Entity data class represents a single row in the database.
 */
@androidx.room.Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val preset: Preset,
    var isEnabled: Boolean
)

