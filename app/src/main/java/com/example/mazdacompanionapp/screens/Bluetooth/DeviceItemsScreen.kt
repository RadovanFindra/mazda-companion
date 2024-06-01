package com.example.mazdacompanionapp.screens.Bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.primarySurface
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.NavigationDestination
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem
import com.example.mazdacompanionapp.screens.DrawerContent
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
    modifier: Modifier = Modifier,
    viewModel: DeviceItemsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onDestinationClicked = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colors.primarySurface,
                        titleContentColor = MaterialTheme.colors.primary,
                    ),
                    title = { Text(stringResource(id = R.string.deviceItems_main_title)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = navigateToDeviceAdd,
                    backgroundColor = MaterialTheme.colors.primarySurface,
                    contentColor = MaterialTheme.colors.surface
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
               val deviceItemsUiState by viewModel.deviceItemsUiState.collectAsState()
                DeviceItemsBody(
                    deviceItemsList = deviceItemsUiState.deviceList,
                    onDevicetClick = { viewModel.changeEnableState(it) },
                    onDeviceDelete = { viewModel.deleteEvent(it) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun DeviceItemsBody(
    deviceItemsList: List<DeviceItem>,
    onDevicetClick: (Int) -> Unit,
    onDeviceDelete: (Int) -> Unit,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (deviceItemsList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_device_list),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding)
            )
        } else {
            DeviceItemsList(
                deviceItemsList = deviceItemsList,
                onEventClick = onDevicetClick,
                onEventDelete = onDeviceDelete,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun DeviceItemsList(
    deviceItemsList: List<DeviceItem>,
    onEventClick: (Int) -> Unit,
    onEventDelete: (Int) -> Unit,
    modifier: Modifier
) {


}