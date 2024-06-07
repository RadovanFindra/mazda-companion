package com.example.mazdacompanionapp.ui.screens.Bluetooth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.ui.Navigation.NavigationDestination
import com.example.mazdacompanionapp.ui.screens.Bluetooth.viewModel.BluetoothDeviceItem
import com.example.mazdacompanionapp.ui.screens.Bluetooth.viewModel.DeviceItemAddViewModel
import kotlinx.coroutines.launch

object DeviceItemAddScreenDestination : NavigationDestination {
    override val route = "Devices_add_screen"
    override val titleRes = R.string.deviceItems_add_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceItemAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: DeviceItemAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.bluetoothManager.startDiscovery()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.tertiaryContainer,
                    titleContentColor = colorScheme.onTertiaryContainer,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.deviceItems_add_title),
                        color = colorScheme.onTertiaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DeviceList(
                onDeviceSelected =
                {
                    coroutineScope.launch {
                        viewModel.saveDevice(it)
                        navigateBack()
                    }
                },
                discoveredDevices = viewModel.bluetoothManager.discoveredDevices
            )
        }
    }
}


@Composable
fun DeviceList(
    onDeviceSelected: (BluetoothDeviceItem) -> Unit,
    discoveredDevices: List<BluetoothDeviceItem>
) {
    LazyColumn {
        items(discoveredDevices) { device ->
            DeviceItem(device = device, onClick = { onDeviceSelected(device) })
        }
    }
}

@Composable
fun DeviceItem(
    device: BluetoothDeviceItem,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .clickable { onClick() }
    ) {
        Text(
            text = device.name ?: "Unknown Device",
            style = MaterialTheme.typography.h6,
            color = colorScheme.onSurface
        )
        Text(
            text = device.address,
            style = MaterialTheme.typography.body2,
            color = colorScheme.onSurface
        )
    }
}

