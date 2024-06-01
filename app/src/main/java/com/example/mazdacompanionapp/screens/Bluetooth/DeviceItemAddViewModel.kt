package com.example.mazdacompanionapp.screens.Bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazdacompanionapp.MyBluetoothManager
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeviceItemAddViewModel(
    private val deviceItemsRepository: DeviceItemsRepository,
    bluetoothManager: MyBluetoothManager
) : ViewModel() {

    private val _deviceItemAddUiState = MutableStateFlow(
        DeviceItemAddUiState()
    )
    val deviceItemAddUiState: StateFlow<DeviceItemAddUiState> = _deviceItemAddUiState

    init {
        viewModelScope.launch {
            bluetoothManager.getDiscoveredDevicesFlow().map { DeviceItemAddUiState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(DeviceItemAddViewModel.TIMEOUT_MILLIS),
                    initialValue = DeviceItemAddUiState()
                ).collect {
                    _deviceItemAddUiState.value = it
                }
        }
    }

    fun updateUiState(bluetoothDeviceItem: BluetoothDeviceItem) {
        _deviceItemAddUiState.value =
            DeviceItemAddUiState(listOf(bluetoothDeviceItem), isAddValid = validateInput(bluetoothDeviceItem))
    }

    suspend fun saveDevice() {
        if (validateInput()) {
            deviceItemsRepository.insertDeviceItem(_deviceItemAddUiState.value.bluetoothDeviceItem.first().toDeviceItem())
        }
    }

    private fun validateInput(uiState: BluetoothDeviceItem = _deviceItemAddUiState.value.bluetoothDeviceItem.firstOrNull() ?: BluetoothDeviceItem()): Boolean {
        return with(uiState) {
            address.isNotBlank()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DeviceItemAddUiState(
    val bluetoothDeviceItem: List<BluetoothDeviceItem> = emptyList(),
    val isAddValid: Boolean = false
)

data class BluetoothDeviceItem(
    val name: String? ="",
    val address: String = ""
)


fun BluetoothDeviceItem.toDeviceItem(): DeviceItem {
    return DeviceItem(
        name = this.name,
        address = this.address,
        events = mutableListOf(),
        isEnabled = false
    )
}