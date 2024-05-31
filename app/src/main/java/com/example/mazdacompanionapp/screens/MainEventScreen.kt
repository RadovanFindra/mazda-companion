package com.example.mazdacompanionapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.NavigationDestination
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.data.UpdateEvents.Event
import kotlinx.coroutines.launch

object MainEventScreenDestination : NavigationDestination {
    override val route = "Main"
    override val titleRes = R.string.event_main_title
}

@Composable
fun MainEventScreen(
    navigateToEventAdd: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(drawerState = drawerState),
        drawerContent = {
            DrawerContent(navController, drawerState, scope)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(MainEventScreenDestination.titleRes)) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "MainEventScreen") {
            composable("MainEventScreen") { MainEventScreen(navController, innerPadding) }
            composable("BluetoothDevicesScreen") { BluetoothDevicesScreen() }
        }
    }
}



@Composable
fun MainEventScreen(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val mainUiState by viewModel.mainUiState.collectAsState()
    MainBody(
        eventList = mainUiState.eventList,
        onEventClick = { viewModel.changeEnableState(it) },
        onEventDelete = { viewModel.deleteEvent(it) },
        modifier = Modifier.fillMaxWidth(),
        contentPadding = innerPadding
    )
}

@Composable
private fun MainBody(
    eventList: List<Event>,
    onEventClick: (Int) -> Unit,
    onEventDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (eventList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_event_list),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding)
            )
        } else {
            EventList(
                eventList = eventList,
                onEventClick = onEventClick,
                onEventDelete = onEventDelete,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun EventList(
    eventList: List<Event>,
    onEventClick: (Int) -> Unit,
    onEventDelete: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(eventList) { event ->
            EventItem(
                event = event,
                onClick = { onEventClick(event.id) },
                onDeleteClick = { onEventDelete(event.id) }
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                onDeleteClick()
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Column(modifier = Modifier
        .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = event.name, style = MaterialTheme.typography.h6)
                Text(text = event.preset.name, style = MaterialTheme.typography.body2)
            }
            Row {
                Switch(
                    checked = event.isEnabled,
                    onCheckedChange = { onClick() }
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Event")
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Event") },
        text = { Text("Are you sure you want to delete this event?") },

        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Delete")
            }
        },

        dismissButton = {
            OutlinedButton(onClick = onDismiss,) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BluetoothDevicesScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Bluetooth Devices Screen")
    }
}
