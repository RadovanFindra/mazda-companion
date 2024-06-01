package com.example.mazdacompanionapp.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.primarySurface
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.NavigationDestination
import com.example.mazdacompanionapp.R
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
    viewModel: EventAddViewModel = viewModel( factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material.MaterialTheme.colors.primarySurface,
                    titleContentColor = androidx.compose.material.MaterialTheme.colors.primary,
                ),
                title = { Text(stringResource(id = R.string.event_add_title)) },
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
        }
    ) {innerPadding ->
        AddEventBody(
            eventUiState = viewModel.eventUiState,
            onEventValueChange = viewModel::updateUiState,
            onSaveClick =
            {
                coroutineScope.launch {
                viewModel.saveEvent()
                navigateBack()
            }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}


@Composable
fun AddEventBody(
    eventUiState: EventUiState,
    onEventValueChange: (EventDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_big))
    ) {
        AddForm(
            eventDetails = eventUiState.eventDetails,
            onEventValueChange = onEventValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = onSaveClick,
            enabled = eventUiState.isAddValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Event")
        }
    }
}


@Composable
fun AddForm(
    eventDetails: EventDetails,
    modifier: Modifier = Modifier,
    onEventValueChange: (EventDetails) -> Unit = {},
    enabled: Boolean = true
) {
    var selectedPreset by remember { mutableStateOf<Preset?>(null) }
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = eventDetails.name,
            onValueChange = { onEventValueChange(eventDetails.copy(name = it)) },
            label = { Text(text = "Name*") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(text = selectedPreset?.title ?: "Select Preset")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Preset.entries.forEach { preset ->
                        DropdownMenuItem(
                            onClick = {
                                selectedPreset = preset
                                expanded = false
                            }
                        ) {
                            Text(text = preset.title)
                        }
                    }
                }
            }
        }
    }
}



