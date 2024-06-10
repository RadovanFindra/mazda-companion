package com.example.mazdacompanionapp.ui.screens.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.ui.Navigation.NavigationDestination
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.BluetoothDeviceItem
import com.example.mazdacompanionapp.ui.screens.devices.viewModel.DeviceItemAddViewModel
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
    val discoveredDevices = viewModel.bluetoothManager.discoveredDevices

    LaunchedEffect(Unit) {
        viewModel.bluetoothManager.startDiscovery()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.bluetoothManager.cancelDiscovery()
        }
    }

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
                onDeviceSelected = {
                    coroutineScope.launch {
                        viewModel.saveDevice(it)
                        navigateBack()
                    }
                },
                discoveredDevices = discoveredDevices
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
        .padding(8.dp)
    ) {
        OutlinedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surfaceVariant,
                contentColor = colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier
                .padding(0.dp)
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = device.name ?: "Unknown Device",
                    style = MaterialTheme.typography.h6,
                    color = colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = device.address,
                    style = MaterialTheme.typography.body2,
                    color = colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
