
package com.example.mazdacompanionapp.data

import android.content.Context
import com.example.mazdacompanionapp.data.UpdateEvents.EventsDatabase
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.OfflineEventsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val eventsRepository: EventsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val eventsRepository: EventsRepository by lazy {
        OfflineEventsRepository(EventsDatabase.getDatabase(context).eventDao())
    }
}
