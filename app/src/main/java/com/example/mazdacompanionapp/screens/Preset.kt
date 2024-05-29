package com.example.mazdacompanionapp.screens

import androidx.compose.runtime.MutableState

data class Event(
    val name: String,
    val preset: Preset,
    var isEnabled: MutableState <Boolean>
)
enum class Preset(val title: String) {
    DEFAULT("Default"),
    MUSIC_MESSAGE("Music Message"),
    EVENTS("Events")
}
