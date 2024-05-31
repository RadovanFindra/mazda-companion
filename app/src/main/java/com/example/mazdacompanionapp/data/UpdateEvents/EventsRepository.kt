
package com.example.mazdacompanionapp.data.UpdateEvents

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Event] from a given data source.
 */
interface EventsRepository {
    /**
     * Retrieve all the events from the the given data source.
     */
    fun getAllEventsStream(): Flow<List<Event>>

    /**
     * Retrieve an event from the given data source that matches with the [id].
     */
    fun getEventStream(id: Int): Flow<Event?>

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
