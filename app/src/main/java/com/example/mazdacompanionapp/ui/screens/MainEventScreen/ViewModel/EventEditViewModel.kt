package com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.InstalledAppsGetter
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.EventEditScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
            _eventEditUiState.value = EventEditUiState(eventDetails, apps)
        }
    }
}

data class EventEditUiState(
    var eventDetails: EventDetails = EventDetails(),
    val apps: List<AppInfo> = emptyList()
)

fun Event.toEventDetails(): EventDetails = EventDetails(
    id = id,
    name = name,
    preset = preset,
    isEnabled = isEnabled,
    selectedApps = selectedApps
)