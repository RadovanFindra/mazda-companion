package com.example.mazdacompanionapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mazdacompanionapp.screens.EventAddViewModel

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
    }
}

fun CreationExtras.companionApplication(): CompanionApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CompanionApplication)