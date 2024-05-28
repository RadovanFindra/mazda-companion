package com.example.mazdacompanionapp

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mazdacompanionapp.ui.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(CompanionAplication().container.eventsRepository)
        }

    }
}