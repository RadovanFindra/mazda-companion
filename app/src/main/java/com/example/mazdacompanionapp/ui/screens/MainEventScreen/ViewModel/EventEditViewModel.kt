package com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.getters.InstalledAppsGetter
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.EventEditScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
/**
 *ViewModel for add Event
 */
class EventEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val deviceItemsRepository: DeviceItemsRepository,
    private val eventsRepository: EventsRepository,
    installedAppsGetter: InstalledAppsGetter
) : ViewModel() {

    private val eventId: Int = checkNotNull(savedStateHandle[EventEditScreenDestination.eventIdArg])

    private val _eventEditUiState = MutableStateFlow(EventEditUiState())
    val eventEditUiState: StateFlow<EventEditUiState> = _eventEditUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val eventDetails = eventsRepository.getEventStream(eventId)
                .filterNotNull()
                .first()
                .toEventDetails()
            val apps = installedAppsGetter.getInstalledApps()
            _eventEditUiState.value = EventEditUiState(eventDetails = eventDetails, apps = apps)
        }
    }

    fun updateUiState(eventDetails: EventDetails) {
        _eventEditUiState.value = _eventEditUiState.value.copy(
            eventDetails = eventDetails,
            isAddValid = validateInput(eventDetails)
        )
    }

    suspend fun updateEvent() {
        viewModelScope.launch {
            if (validateInput()) {
                eventsRepository.updateEvent(eventEditUiState.value.eventDetails.toEvent())
            }

            val event = eventsRepository.getEventStream(eventId).firstOrNull()
            event?.let {
                val updatedEvent = it.copy(isEnabled = it.isEnabled)
                eventsRepository.updateEvent(updatedEvent)

                val devices =
                    deviceItemsRepository.getAllDeviceItemsStream().firstOrNull() ?: emptyList()
                val updatedDevices = devices.map { device ->
                    val updatedEvents = device.events.map { evt ->
                        if (evt.id == updatedEvent.id) updatedEvent else evt
                    }
                    device.copy(events = updatedEvents.toMutableList())
                }
                updatedDevices.forEach { device ->
                    deviceItemsRepository.updateDeviceItem(device)
                }
            }
        }
    }


    private fun validateInput(uiState: EventDetails = eventEditUiState.value.eventDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class EventEditUiState(
    var eventDetails: EventDetails = EventDetails(),
    val isAddValid: Boolean = true,
    val apps: List<AppInfo> = emptyList()
)

fun Event.toEventDetails(): EventDetails = EventDetails(
    id = id,
    name = name,
    preset = preset,
    isEnabled = isEnabled,
    selectedApps = selectedApps.toMutableList()
)