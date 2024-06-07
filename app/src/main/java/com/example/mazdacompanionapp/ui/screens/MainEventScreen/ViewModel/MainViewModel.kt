package com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val eventsRepository: EventsRepository,
    private val devicesRepository: DeviceItemsRepository
) : ViewModel() {

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

    fun deleteEvent(id: Int) {
        viewModelScope.launch {
            val event = eventsRepository.getEventStream(id).firstOrNull()
            event?.let {
                val devices = devicesRepository.getAllDeviceItemsStream().firstOrNull() ?: emptyList()
                val updatedDevices = devices.map { device ->
                    val updatedEvents = device.events.filter { it.id != event.id }
                    device.copy(events = updatedEvents.toMutableList())
                }
                updatedDevices.forEach { device ->
                    devicesRepository.updateDeviceItem(device)
                }
                eventsRepository.deleteEvent(event)
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