
package com.example.mazdacompanionapp.data.UpdateEvents

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EventsDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var Instance: EventsDatabase? = null

        fun getDatabase(context: Context): EventsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, EventsDatabase::class.java, "events_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

