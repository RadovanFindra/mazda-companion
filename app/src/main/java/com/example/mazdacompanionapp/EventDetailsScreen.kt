package com.example.mazdacompanionapp

import com.example.mazdacompanionapp.navigation.NavigationDestination

object EventDetailsDestination : NavigationDestination {
    override val route = "event_details"
    override val titleRes = R.string.event_detail_title
    const val eventIdArg = "eventId"
    val routeWithArgs = "$route/{$eventIdArg}"
}
