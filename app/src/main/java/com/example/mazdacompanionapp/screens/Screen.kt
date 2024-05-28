package com.example.mazdacompanionapp

sealed class Screens(val route: String) {
    object MainScreen : Screens("main_screen")
    object AddEventScreen : Screens("add_event_screen")
    object PresetSelectionScreen : Screens("preset_selection_screen")
}