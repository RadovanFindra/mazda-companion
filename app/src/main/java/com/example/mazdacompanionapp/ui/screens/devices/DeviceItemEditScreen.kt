package com.example.mazdacompanionapp.ui.screens.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import com.example.mazdacompanionapp.ui.Navigation.NavigationDestination
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.DeviceDetails
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.DeviceItemEditViewModel
import kotlinx.coroutines.launch

object DeviceItemEditScreenDestination : NavigationDestination {
    override val route = "Devices_edit_screen"
    override val titleRes = R.string.deviceItems_edit_title
    const val deviceIdArg = "deviceId"
    val routeWithArgs = "$route/{$deviceIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBluetoothItem(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeviceItemEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.deviceItemEditUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.tertiaryContainer,
                    titleContentColor = colorScheme.onTertiaryContainer,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.deviceItems_edit_title),
                        color = colorScheme.onTertiaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        },
        modifier = modifier,
    ) { innerPadding ->

        EditBluetoothItemBody(
            deviceDetails = uiState.deviceDetails,
            onDeviceValueChange = viewModel::updateUiState,
            events = uiState.events,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateDevice(uiState.deviceDetails)
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )

        )
    }
}

@Composable
fun EditBluetoothItemBody(
    deviceDetails: DeviceDetails,
    onDeviceValueChange: (DeviceDetails) -> Unit,
    events: List<Event>,
    onSaveClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_big))
    ) {
        DeviceEditForm(
            deviceDetails = deviceDetails,
            events = events,
            onDeviceChange = onDeviceValueChange
        )
        OutlinedButton(
            onClick = onSaveClick,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colorScheme.tertiary,
                contentColor = colorScheme.onSurface,
                disabledContainerColor = colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Device")
        }
    }
}

@Composable
fun DeviceEditForm(
    deviceDetails: DeviceDetails,
    events: List<Event>,
    onDeviceChange: (DeviceDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        deviceDetails.name?.let { name ->
            OutlinedTextField(
                value = name,
                onValueChange = { onDeviceChange(deviceDetails.copy(name = it)) },
                label = { Text(text = "Name", color = colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.onTertiary,
                    unfocusedContainerColor = colorScheme.tertiaryContainer
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
        OutlinedTextField(
            value = deviceDetails.address,
            onValueChange = { },
            label = { Text(text = "Address", color = colorScheme.onSurface) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorScheme.onTertiary,
                unfocusedContainerColor = colorScheme.tertiaryContainer,
                disabledContainerColor = colorScheme.onTertiary
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = false
        )
        AssignEvents(
            events = events,
            deviceDetails = deviceDetails,
            onDeviceChange = onDeviceChange
        )

    }
}

@Composable
fun AssignEvents(
    events: List<Event>,
    deviceDetails: DeviceDetails,
    onDeviceChange: (DeviceDetails) -> Unit
) {
    LazyColumn {
        items(events) { event ->
            val containsEvent = deviceDetails.events.find { event1 -> event1.id == event.id }!=null
            EventItemAssign(
                event = event,
                contains = containsEvent,
                onCheckedChange = { isChecked ->
                    val updatedEvents = if (isChecked) {
                        deviceDetails.events + event
                    } else {
                        deviceDetails.events.filter { it.id != event.id }
                    }
                    onDeviceChange(deviceDetails.copy(events = updatedEvents))
                }
            )
        }
    }
}

@Composable
fun EventItemAssign(
    event: Event,
    contains: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                event.name,
                color = colorScheme.onSurface
            )
        },
        trailingContent = {
            Checkbox(
                checked = contains,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorScheme.tertiary,
                    uncheckedColor = colorScheme.onSecondaryContainer
                )
            )
        }
    )
}
