package com.example.mazdacompanionapp.ui.screens.Bluetooth.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.ui.screens.Bluetooth.DeviceItemEditScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeviceItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val deviceItemsRepository: DeviceItemsRepository,
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val deviceId: Int = checkNotNull(savedStateHandle[DeviceItemEditScreenDestination.deviceIdArg])

    private val _deviceItemEditUiState = MutableStateFlow(DeviceItemEditUiState())
    val deviceItemEditUiState: StateFlow<DeviceItemEditUiState> = _deviceItemEditUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val deviceDetails = deviceItemsRepository.getDeviceItemStream(deviceId)
                .filterNotNull()
                .first()
                .toDeviceDetails()
            val events = eventsRepository.getAllEventsStream().first()
            _deviceItemEditUiState.value = DeviceItemEditUiState(deviceDetails, events)
        }
    }

    fun updateUiState(deviceDetails: DeviceDetails) {
        _deviceItemEditUiState.value = _deviceItemEditUiState.value.copy(deviceDetails = deviceDetails)
    }

    suspend fun updateDevice(deviceDetails: DeviceDetails) {
        deviceItemsRepository.updateDeviceItem(deviceDetails.toDevice())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DeviceItemEditUiState(
    var deviceDetails: DeviceDetails = DeviceDetails(),
    val events: List<Event> = emptyList()
)

data class DeviceDetails(
    val id: Int = 0,
    val name: String? = "",
    val address: String = "",
    val events: List<Event> = mutableListOf(),
    val isEnabled: Boolean = false
)

fun DeviceItem.toDeviceDetails(): DeviceDetails = DeviceDetails(
    id = id,
    name = name,
    address = address,
    events = events.toMutableList(),
    isEnabled = isEnabled
)

fun DeviceItem.toDeviceItemEditUiState(): DeviceItemEditUiState = DeviceItemEditUiState(
    deviceDetails = this.toDeviceDetails(),
    events = events
)

fun DeviceDetails.toDevice(): DeviceItem = DeviceItem(
    id = id,
    name = name,
    address = address,
    events = events.toMutableList(),
    isEnabled = isEnabled
)