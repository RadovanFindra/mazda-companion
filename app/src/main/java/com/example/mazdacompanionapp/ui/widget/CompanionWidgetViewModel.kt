package com.example.mazdacompanionapp.ui.widget

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

class CompanionWidgetViewModel(private val devicesRepository: DeviceItemsRepository) : ViewModel() {

    val companionWidgetUiState: StateFlow<CompanionWidgetUiState> =
        devicesRepository.getAllDeviceItemsStream().map { CompanionWidgetUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CompanionWidgetUiState()
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

data class CompanionWidgetUiState(
    val deviceList: List<DeviceItem> = listOf()
)