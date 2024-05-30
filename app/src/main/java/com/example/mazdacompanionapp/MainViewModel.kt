package com.example.mazdacompanionapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.data.Event
import com.example.mazdacompanionapp.data.EventsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel( private val eventsRepository: EventsRepository) : ViewModel() {

    val mainUiState: StateFlow<MainUiState> =
        eventsRepository.getAllEventsStream().map { MainUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainUiState()
            )
    fun changeEnableState(id: Int) {

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MainUiState(
    val eventList: List<Event> = listOf()
)