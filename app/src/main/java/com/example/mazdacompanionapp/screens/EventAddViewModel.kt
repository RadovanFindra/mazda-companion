package com.example.mazdacompanionapp.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mazdacompanionapp.data.Event
import com.example.mazdacompanionapp.data.EventsRepository

class EventAddViewModel(private val eventsRepository: EventsRepository): ViewModel() {
var eventUiState by mutableStateOf(EventUiState())
    private set

    fun updateUiState(eventDetails: EventDetails) {
        eventUiState =
            EventUiState(eventDetails = eventDetails, isAddValid = validateInput(eventDetails))
    }
    suspend fun saveEvent() {
        if (validateInput()) {
            eventsRepository.insertEvent(eventUiState.eventDetails.toEvent())
        }
    }
    private fun validateInput(uiState: EventDetails = eventUiState.eventDetails): Boolean{
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class EventUiState(
    val eventDetails: EventDetails = EventDetails(),
    val isAddValid : Boolean = false
)

data class EventDetails(
    val id: Int = 0,
    val name: String ="",
    val preset: Preset? = null,
    var isEnabled: Boolean = true
)

fun EventDetails.toEvent(): Event = Event(
    id = id,
    name = name,
    preset = preset ?: Preset.DEFAULT,
    isEnabled = isEnabled
)