
package com.example.mazdacompanionapp.data.BluetoothDevices

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [DeviceItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DeviceItemsDatabase : RoomDatabase() {

    abstract fun deviceItemDao(): DeviceItemDao

    companion object {
        @Volatile
        private var Instance: DeviceItemsDatabase? = null

        fun getDatabase(context: Context): DeviceItemsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DeviceItemsDatabase::class.java, "deviceItems_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

