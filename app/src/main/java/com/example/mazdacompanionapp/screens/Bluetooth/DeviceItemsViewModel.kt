package com.example.mazdacompanionapp.screens.Bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeviceItemsViewModel( private val devicesRepository: DeviceItemsRepository) : ViewModel() {

    val deviceItemsUiState: StateFlow<DeviceItemsUiState> =
        devicesRepository.getAllDeviceItemsStream().map { DeviceItemsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DeviceItemsUiState()
            )

    fun changeEnableState(id: Int) {
        viewModelScope.launch {
            val event = devicesRepository.getDeviceItemStream(id).firstOrNull()
            event?.let {
                val updatedDeviceItem = it.copy(isEnabled = !it.isEnabled)
                devicesRepository.updateDeviceItem(updatedDeviceItem)
            }
        }
    }

    fun deleteDevice(id: Int) {
        viewModelScope.launch {
            val device = devicesRepository.getDeviceItemStream(id).firstOrNull()
            device?.let {
                devicesRepository.deleteDeviceItem(device)
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DeviceItemsUiState(
    val deviceList: List<DeviceItem> = listOf()
)