package com.example.mazdacompanionapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column {
        Text(
            text = "Navigation",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(16.dp)
        )
        Divider()
        ListItem(
            text = { Text("Events") },
            modifier = Modifier.clickable {
                scope.launch {
                    drawerState.close()
                    navController.navigate("MainEventScreen")
                }
            }
        )
        ListItem(
            text = { Text("Bluetooth Devices") },
            modifier = Modifier.clickable {
                scope.launch {
                    drawerState.close()
                    navController.navigate("BluetoothDevicesScreen")
                }
            }
        )
    }
}