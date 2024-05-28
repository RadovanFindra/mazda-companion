package com.example.mazdacompanionapp

data class Event(val name: String, val preset: Preset, val sendEvent: String)

enum class Preset(val title: String) {
    DEFAULT("Default"),
    MUSIC_MESSAGE("Music Message"),
    EVENTS("Events")
}
