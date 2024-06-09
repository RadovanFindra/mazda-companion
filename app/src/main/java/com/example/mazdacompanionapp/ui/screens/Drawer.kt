package com.example.mazdacompanionapp.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemsScreenDestination
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.MainEventScreenDestination

@Composable

fun DrawerContent(
    navController: NavHostController,
    onDestinationClicked: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        drawerContentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text("Navigation", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Spacer(Modifier.height(12.dp))
        Divider(thickness = 3.dp)
        NavigationDrawerItem(
            label = { Text(text = "Events", color =  MaterialTheme.colorScheme.onSurface) },
            selected = true,
            onClick = { onDestinationClicked(MainEventScreenDestination.route) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
        )
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        NavigationDrawerItem(
            label = { Text(text = "Bluetooth",color =  MaterialTheme.colorScheme.onSurface) },
            selected = true,
            onClick = { onDestinationClicked(DeviceItemsScreenDestination.route) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )
    }
}