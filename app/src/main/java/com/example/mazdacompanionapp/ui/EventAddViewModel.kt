package com.example.mazdacompanionapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mazdacompanionapp.Event
import com.example.mazdacompanionapp.Preset
import com.example.mazdacompanionapp.data.EventsRepository

class EventAddViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    /**
     * Holds current event ui state
     */
    var eventUiState by mutableStateOf(EventUiState())
        private set

    /**
     * Updates the [eventUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(eventDetails: EventDetails) {
        eventUiState =
            EventUiState(eventDetails = eventDetails, isEntryValid = validateInput(eventDetails))
    }

    /**
     * Inserts an [Event] in the Room database
     */
    suspend fun saveEvent() {
        if (validateInput()) {
            eventsRepository.insertEvent(eventUiState.eventDetails.toEvent())
        }
    }

    private fun validateInput(uiState: EventDetails = eventUiState.eventDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Event.
 */
data class EventUiState(
    val eventDetails: EventDetails = EventDetails(),
    val isEntryValid: Boolean = false
)

data class EventDetails(
    val id: Int = 0,
    val name: String = "",
    val preset: Preset = Preset.DEFAULT,
    val sendEvent: String = ""
)


fun EventDetails.toEvent(): Event = Event(
    id = id,
    name = name,
    preset = preset,
    sendEvent = sendEvent,

)

/**
 * Extension function to convert [Event] to [EventUiState]
 */
fun Event.toEventUiState(isEntryValid: Boolean = false): EventUiState = EventUiState(
    eventDetails = this.toEventDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Event] to [EventDetails]
 */
fun Event.toEventDetails(): EventDetails = EventDetails(
    id = id,
    name = name,
    preset = preset,
    sendEvent = sendEvent
)