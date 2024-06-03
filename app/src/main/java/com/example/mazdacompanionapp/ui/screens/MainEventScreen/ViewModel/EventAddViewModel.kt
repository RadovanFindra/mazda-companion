package com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.data.UpdateEvents.EventsRepository
import com.example.mazdacompanionapp.data.UpdateEvents.SEND_EVENT_PRESET
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventAddViewModel(private val eventsRepository: EventsRepository,
    private val context: Context): ViewModel() {


    private val _eventUiState = MutableStateFlow(EventUiState(installedApps = getInstalledApps(context)))
    val eventUiState: StateFlow<EventUiState> = _eventUiState.asStateFlow()

    fun updateUiState(eventDetails: EventDetails) {
        _eventUiState.value = _eventUiState.value.copy(eventDetails = eventDetails, isAddValid = validateInput(eventDetails))
    }

    suspend fun saveEvent() {
        if (validateInput()) {
            eventsRepository.insertEvent(eventUiState.value.eventDetails.toEvent())
        }
    }
    private fun validateInput(uiState: EventDetails = eventUiState.value.eventDetails): Boolean{
        return with(uiState) {
            name.isNotBlank()
        }
    }

    private fun getInstalledApps(context: Context): List<AppInfo> {
        val packageManager: PackageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

        return resolveInfoList.map { resolveInfo ->
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val appIcon = resolveInfo.loadIcon(packageManager)
            AppInfo(name = appName, icon = appIcon.toBitmap())
        }.sortedBy { it.name }
    }
}

data class EventUiState(
    val eventDetails: EventDetails = EventDetails(),
    val isAddValid : Boolean = false,
    val installedApps: List<AppInfo>
)

data class AppInfo(
    val name: String,
    val icon: Bitmap
)

data class EventDetails(
    val id: Int = 0,
    val name: String ="",
    var preset: SEND_EVENT_PRESET? = null,
    val selectedApps: List<AppInfo> = emptyList() ,
    var isEnabled: Boolean = true
)

fun EventDetails.toEvent(): Event = Event(
    id = id,
    name = name,
    preset = preset ?: SEND_EVENT_PRESET.DEFAULT,
    selectedApps = selectedApps,
    isEnabled = isEnabled
)