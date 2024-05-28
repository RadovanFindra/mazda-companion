package com.example.mazdacompanionapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    // Presets
    private val _presets = MutableLiveData<List<Preset>>(emptyList())
    val presets: LiveData<List<Preset>> get() = _presets

    // Selected preset
    private val _selectedPreset = MutableLiveData<Preset?>(null)
    val selectedPreset: LiveData<Preset?> get() = _selectedPreset

    // Notifications
    private val _notifications = MutableLiveData<List<NotificationData>>(emptyList())
    val notifications: LiveData<List<NotificationData>> get() = _notifications

    fun addPreset(preset: Preset) {
        _presets.value = _presets.value.orEmpty() + preset
    }

    fun selectPreset(preset: Preset?) {
        _selectedPreset.value = preset
    }

    fun sendNotifications() {
        //TODO: Implement
    }
}