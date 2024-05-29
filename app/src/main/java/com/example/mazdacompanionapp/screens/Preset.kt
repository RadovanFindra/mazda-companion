package com.example.mazdacompanionapp

import androidx.compose.runtime.MutableState

data class Event(
    val name: String,
    val preset: Preset,
    var isEnabled: MutableState <Boolean>
)
enum class Preset(val title: String) {
    NOT_SELECTED("Select Preset"),
    DEFAULT("Default"),
    MUSIC_MESSAGE("Music Message"),
    EVENTS("Events")
}
