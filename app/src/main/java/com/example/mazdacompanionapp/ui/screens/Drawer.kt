package com.example.mazdacompanionapp.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.MainEventScreenDestination
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemsScreenDestination

@Composable
fun DrawerContent(
    navController: NavHostController,
    onDestinationClicked: (String) -> Unit
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    ModalDrawerSheet(
        drawerContainerColor = colorScheme.secondaryContainer,
        drawerContentColor = colorScheme.onSecondaryContainer
    ) {

        Text(
            "Navigation",
            modifier = Modifier.padding(16.dp),
            color = colorScheme.onSecondaryContainer
        )
        Spacer(Modifier.height(12.dp))
        Divider(thickness = 3.dp)
        NavigationDrawerItem(
            label = { Text(text = "Events", color = colorScheme.onSurface) },
            selected = currentRoute == MainEventScreenDestination.route,
            onClick = { onDestinationClicked(MainEventScreenDestination.route) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = colorScheme.tertiaryContainer,
                selectedTextColor = colorScheme.onTertiaryContainer,
                unselectedContainerColor = colorScheme.secondaryContainer,
                unselectedTextColor = colorScheme.onSecondaryContainer,
            ),
            shape = RoundedCornerShape(8.dp),
        )
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        NavigationDrawerItem(
            label = { Text(text = "Bluetooth", color = colorScheme.onSurface) },
            selected = currentRoute == DeviceItemsScreenDestination.route,
            onClick = { onDestinationClicked(DeviceItemsScreenDestination.route) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = colorScheme.tertiaryContainer,
                selectedTextColor = colorScheme.onTertiaryContainer,
                unselectedContainerColor = colorScheme.secondaryContainer,
                unselectedTextColor = colorScheme.onSecondaryContainer,
            ),
            shape = RoundedCornerShape(8.dp),
        )
    }
}