package com.example.mazdacompanionapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DrawerContent(
    navController: NavHostController,
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("Navigation", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Events") },
                    selected = false,
                    onClick = { navController.navigate(EventAddDestination.route)}
                )
                NavigationDrawerItem(
                    label = { Text(text = "Bluetooth") },
                    selected = true,
                    onClick = { navController.navigate(EventAddDestination.route)}
                )
            }
        },
        gesturesEnabled = true
    ) {

    }
}