package com.example.mazdacompanionapp.data

import android.content.Context

interface AppContainer {
    val eventsRepository: EventsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineEventsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [EventsRepository]
     */
    override val eventsRepository: EventsRepository by lazy {
        OfflineEventsRepository(EventsDatabase.getDatabase(context).EventDao())
    }
}