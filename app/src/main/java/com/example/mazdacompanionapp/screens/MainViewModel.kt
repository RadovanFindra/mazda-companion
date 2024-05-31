package com.example.mazdacompanionapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel( private val eventsRepository: EventsRepository) : ViewModel() {

    val mainUiState: StateFlow<MainUiState> =
        eventsRepository.getAllEventsStream().map { MainUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainUiState()
            )
    fun changeEnableState(id: Int) {
        viewModelScope.launch {
            val event = eventsRepository.getEventStream(id).firstOrNull()
            event?.let {
                val updatedEvent = it.copy(isEnabled = !it.isEnabled)
                eventsRepository.updateEvent(updatedEvent)
            }
        }


    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MainUiState(
    val eventList: List<Event> = listOf()
)