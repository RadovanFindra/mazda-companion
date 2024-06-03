package com.example.mazdacompanionapp.ui.screens.MainEventScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.NavigationDestination
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.data.UpdateEvents.SEND_EVENT_PRESET
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.AppInfo
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.EventAddViewModel
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.EventDetails
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.EventUiState
import kotlinx.coroutines.launch

object EventAddDestination : NavigationDestination {
    override val route = "event_add"
    override val titleRes = R.string.event_add_title
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddNewEventScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: EventAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.eventUiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.tertiaryContainer,
                    titleContentColor = colorScheme.onTertiaryContainer,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.event_add_title),
                        color = colorScheme.onTertiaryContainer
                    )
                },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = onNavigateUp) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->
        AddEventBody(
            eventUiState = uiState,
            onEventValueChange = viewModel::updateUiState,
            onSaveClick =
            {
                coroutineScope.launch {
                    viewModel.saveEvent()
                    navigateBack()
                }
            },
            installedApps = uiState.installedApps,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .fillMaxWidth()
        )
    }
}

@Composable
fun AddEventBody(
    eventUiState: EventUiState,
    onEventValueChange: (EventDetails) -> Unit,
    installedApps: List<AppInfo>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_big))
    ) {
        item {
            AddForm(
                eventDetails = eventUiState.eventDetails,
                onEventValueChange = onEventValueChange,
                installedApps = installedApps,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedButton(
                onClick = onSaveClick,
                enabled = eventUiState.isAddValid,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colorScheme.surfaceTint,
                    contentColor = colorScheme.onSurface,
                    disabledContainerColor = colorScheme.surface,
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (eventUiState.isAddValid) {
                    Text(text = "Save Event")
                } else {
                    Text(text = "Save Event", color = colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
fun AddForm(
    eventDetails: EventDetails,
    modifier: Modifier = Modifier,
    onEventValueChange: (EventDetails) -> Unit = {},
    installedApps: List<AppInfo>,
    enabled: Boolean = true
) {
    var selectedPreset by remember { mutableStateOf<SEND_EVENT_PRESET?>(null) }
    var expandedPresets by remember { mutableStateOf(false) }
    var expandedApps by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = eventDetails.name,
            onValueChange = { onEventValueChange(eventDetails.copy(name = it)) },
            label = { Text(text = "Name*", color = colorScheme.onSurface) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorScheme.secondaryContainer,
                unfocusedContainerColor = colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
            ) {
                OutlinedButton(
                    onClick = { expandedPresets = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorScheme.surfaceTint,
                        contentColor = colorScheme.onSurface,
                        disabledContainerColor = colorScheme.surface,
                    ),
                ) {
                    Text(text = eventDetails.preset?.title ?: "Select Preset")
                }
                DropdownMenu(
                    expanded = expandedPresets,
                    onDismissRequest = { expandedPresets = false }
                ) {
                    SEND_EVENT_PRESET.entries.forEach { preset ->
                        DropdownMenuItem(
                            onClick = {
                                selectedPreset = preset
                                eventDetails.preset = selectedPreset
                                expandedPresets = false
                            },
                        ) {
                            Text(text = preset.title, color = colorScheme.onSurface)
                        }
                    }
                }
            }
            Box(

            ) {
                OutlinedButton(
                    onClick = { expandedApps = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorScheme.surfaceTint,
                        contentColor = colorScheme.onSurface,
                        disabledContainerColor = colorScheme.surface,
                    ),
                ) {
                    Text(text = "Select Apps")
                }
                DropdownMenu(
                    expanded = expandedApps,
                    onDismissRequest = { expandedApps = false },
                    modifier = Modifier
                        .height(600.dp)
                        .width(
                            installedApps.maxBy { app -> app.name.length.dp }.name.length.dp
                                .times(
                                    11
                                )
                                .plus(50.dp)
                        )

                ) {
                    installedApps.forEach { app ->
                        DropdownMenuItem(
                            onClick = { },
                        ) {
                            val containsApp = eventDetails.selectedApps.contains(app)
                            App(app = app,
                                contains = containsApp,
                                onCheckedChange = { isChecked ->
                                    val updatedApps = if (isChecked) {
                                        eventDetails.selectedApps + app
                                    } else {
                                        eventDetails.selectedApps - app
                                    }
                                    onEventValueChange(eventDetails.copy(selectedApps = updatedApps))
                                })
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@Composable
fun App(
    app: AppInfo,
    contains: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(text = app.name, color = colorScheme.onSurface)

        },
        leadingContent = {
            Image(
                bitmap = app.icon.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
        },

        trailingContent = {
            Checkbox(checked = contains, onCheckedChange = { onCheckedChange(it) })
        }
    )
}

