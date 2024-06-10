package com.example.mazdacompanionapp.ui.screens.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.ui.Navigation.NavigationDestination
import com.example.mazdacompanionapp.ui.screens.DrawerContent
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ConfirmDeleteDialog
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.DeviceItemsViewModel
import kotlinx.coroutines.launch

object DeviceItemsScreenDestination : NavigationDestination {
    override val route = "Devices"
    override val titleRes = R.string.deviceItems_main_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceItemsScreen(
    navController: NavHostController,
    navigateToDeviceAdd: () -> Unit,
    navigateToDeviceEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeviceItemsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(navController = navController, onDestinationClicked = { route ->
            navController.navigate(route) {
                launchSingleTop = true
            }
            coroutineScope.launch {
                drawerState.close()
            }
        })
    }) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.tertiaryContainer,
                titleContentColor = colorScheme.onTertiaryContainer,
            ), title = {
                Text(
                    stringResource(id = R.string.deviceItems_main_title),
                    color = colorScheme.onTertiaryContainer
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = colorScheme.onSurface)
                }
            })
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToDeviceAdd,
                backgroundColor = colorScheme.tertiaryContainer,
                contentColor = colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val deviceItemsUiState by viewModel.deviceItemsUiState.collectAsState()
                DeviceItemsBody(
                    deviceItemsList = deviceItemsUiState.deviceList,
                    onDevicetSwitch = { viewModel.changeEnableState(it) },
                    onDevicetClick = { navigateToDeviceEdit(it) },
                    onDeviceDelete = { viewModel.deleteDevice(it) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun DeviceItemsBody(
    deviceItemsList: List<DeviceItem>,
    onDevicetSwitch: (Int) -> Unit,
    onDevicetClick: (Int) -> Unit,
    onDeviceDelete: (Int) -> Unit,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (deviceItemsList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_device_list),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding),
                color = colorScheme.onSurface
            )
        } else {
            DeviceItemsList(
                deviceItemsList = deviceItemsList,
                onItemSwitch = onDevicetSwitch,
                onItemEdit = onDevicetClick,
                onItemDelete = onDeviceDelete,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun DeviceItemsList(
    deviceItemsList: List<DeviceItem>,
    onItemEdit: (Int) -> Unit,
    onItemSwitch: (Int) -> Unit,
    onItemDelete: (Int) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        items(deviceItemsList) { item ->
            BluetoothItem(item = item,
                onItemEdit = { onItemEdit(item.id) },
                onItemSwitch = { onItemSwitch(item.id) },
                onItemDelete = { onItemDelete(item.id) })
        }
    }
}

@Composable
fun BluetoothItem(
    item: DeviceItem,
    onItemEdit: () -> Unit,
    onItemSwitch: () -> Unit,
    onItemDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ConfirmDeleteDialog(onConfirm = {
            onItemDelete()
            showDialog = false
        }, onDismiss = {
            showDialog = false
        }, "Delete Device?"
        )
    }
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        OutlinedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surfaceVariant,
                contentColor = colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier.padding(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                item.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.h6,
                        color = colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onItemEdit() }
                    ) {
                        Icon(
                            Icons.Default.Edit, contentDescription = "Edit",
                            tint = colorScheme.onSurface
                        )

                    }
                    Switch(
                        checked = item.isEnabled,
                        onCheckedChange = { onItemSwitch() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorScheme.tertiary,
                            uncheckedThumbColor = colorScheme.onSecondaryContainer,
                            uncheckedTrackColor = colorScheme.surface,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Device",
                            tint = colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}


