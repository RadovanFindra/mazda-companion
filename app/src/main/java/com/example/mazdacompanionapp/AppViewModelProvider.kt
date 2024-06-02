package com.example.mazdacompanionapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mazdacompanionapp.ui.screens.Bluetooth.viewModel.DeviceItemAddViewModel
import com.example.mazdacompanionapp.ui.screens.Bluetooth.viewModel.DeviceItemEditViewModel
import com.example.mazdacompanionapp.ui.screens.Bluetooth.viewModel.DeviceItemsViewModel
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.EventAddViewModel
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.MainViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            EventAddViewModel(
                companionApplication().container.eventsRepository
            )
        }
        initializer {
            MainViewModel(
                companionApplication().container.eventsRepository
            )
        }
        initializer {
            DeviceItemsViewModel(
                companionApplication().container.deviceItemsRepository
            )
        }
        initializer {
            DeviceItemAddViewModel(
                companionApplication().container.deviceItemsRepository,
                companionApplication().bluetoothManager
            )
        }
        initializer {
            DeviceItemEditViewModel(
                this.createSavedStateHandle(),
                companionApplication().container.deviceItemsRepository,
                companionApplication().container.eventsRepository
            )
        }
    }
}


fun CreationExtras.companionApplication(): CompanionApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CompanionApplication)