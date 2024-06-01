package com.example.mazdacompanionapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mazdacompanionapp.screens.Bluetooth.DeviceItemsViewModel
import com.example.mazdacompanionapp.screens.EventAddViewModel
import com.example.mazdacompanionapp.screens.MainViewModel

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
    }
}


fun CreationExtras.companionApplication(): CompanionApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CompanionApplication)