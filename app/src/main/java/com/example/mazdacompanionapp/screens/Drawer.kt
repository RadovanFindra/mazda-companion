package com.example.mazdacompanionapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mazdacompanionapp.screens.Bluetooth.DeviceItemsScreenDestination

@Composable

fun DrawerContent(
    navController: NavHostController,
    onDestinationClicked: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        drawerContentColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text("Navigation", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Events") },
            selected = false,
            onClick = { onDestinationClicked(MainEventScreenDestination.route) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
        )
        NavigationDrawerItem(
            label = { Text(text = "Bluetooth") },
            selected = false,
            onClick = { onDestinationClicked(DeviceItemsScreenDestination.route) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )
    }
}