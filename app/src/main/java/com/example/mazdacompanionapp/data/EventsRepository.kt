package com.example.mazdacompanionapp.data

import com.example.mazdacompanionapp.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    /**
     * Retrieve all the events from the the given data source.
     */
    fun getAllEventsStream(): Flow<List<Event>>

    /**
     * Retrieve an event from the given data source that matches with the [id].
     */
    fun getEventStream(): Flow<List<Event>>

    /**
     * Insert event in the data source
     */
    suspend fun insertEvent(event: Event)

    /**
     * Delete event from the data source
     */
    suspend fun deleteEvent(event: Event)

    /**
     * Update event in the data source
     */
    suspend fun updateEvent(event: Event)
}