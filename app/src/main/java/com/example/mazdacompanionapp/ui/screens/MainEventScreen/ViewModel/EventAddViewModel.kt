package com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.SEND_EVENT_PRESET

class EventAddViewModel(private val eventsRepository: EventsRepository,
    private val context: Context): ViewModel() {
var eventUiState by mutableStateOf(EventUiState(installedApps = getInstalledApps(context)))
    private set

     fun updateUiState(eventDetails: EventDetails) {
        eventUiState =
            EventUiState(eventDetails = eventDetails, isAddValid = validateInput(eventDetails), installedApps = getInstalledApps(context = context))
    }
    suspend fun saveEvent() {
        if (validateInput()) {
            eventsRepository.insertEvent(eventUiState.eventDetails.toEvent())
        }
    }
    private fun validateInput(uiState: EventDetails = eventUiState.eventDetails): Boolean{
        return with(uiState) {
            name.isNotBlank()
        }
    }

    private fun getInstalledApps(context: Context): List<String> {
        val packageManager: PackageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)


        // get list of all the apps installed
        val ril = packageManager.queryIntentActivities(mainIntent, 0)
        val componentList: List<String> = ArrayList()
        lateinit var name: String

        // get size of ril and create a list
        val apps = mutableListOf<String>()
        for (ri in ril) {
            if (ri.activityInfo != null) {
                // get package
                val res = packageManager.getResourcesForApplication(ri.activityInfo.applicationInfo)
                // if activity label res is found
                name = if (ri.activityInfo.labelRes != 0) {
                    res.getString(ri.activityInfo.labelRes)
                } else {
                    ri.activityInfo.applicationInfo.loadLabel(packageManager).toString()
                }
                    apps.add(name)
            }
        }
        apps.sort()
        return apps
    }
}

data class EventUiState(
    val eventDetails: EventDetails = EventDetails(),
    val isAddValid : Boolean = false,
    val installedApps: List<String>
)

data class EventDetails(
    val id: Int = 0,
    val name: String ="",
    var preset: SEND_EVENT_PRESET? = null,
    val selectedApps: List<String> = emptyList() ,
    var isEnabled: Boolean = true
)

fun EventDetails.toEvent(): Event = Event(
    id = id,
    name = name,
    preset = preset ?: SEND_EVENT_PRESET.DEFAULT,
    selectedApps = selectedApps,
    isEnabled = isEnabled
)