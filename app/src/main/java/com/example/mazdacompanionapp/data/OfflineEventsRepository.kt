
package com.example.mazdacompanionapp.data

import kotlinx.coroutines.flow.Flow

class OfflineEventsRepository(private val eventDao: EventDao) : EventsRepository {
    override fun getAllEventsStream(): Flow<List<Event>> = eventDao.getAllEvents()

    override fun getEventStream(id: Int): Flow<Event?> = eventDao.getEvent(id)

    override suspend fun insertEvent(event: Event) = eventDao.insert(event)

    override suspend fun deleteEvent(event: Event) = eventDao.delete(event)

    override suspend fun updateEvent(event: Event) = eventDao.update(event)
}
