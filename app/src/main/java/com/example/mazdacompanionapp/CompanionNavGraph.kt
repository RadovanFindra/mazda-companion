package com.example.mazdacompanionapp

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mazdacompanionapp.ui.screens.Bluetooth.DeviceItemAddScreen
import com.example.mazdacompanionapp.ui.screens.Bluetooth.DeviceItemAddScreenDestination
import com.example.mazdacompanionapp.ui.screens.Bluetooth.DeviceItemEditScreenDestination
import com.example.mazdacompanionapp.ui.screens.Bluetooth.DeviceItemsScreen
import com.example.mazdacompanionapp.ui.screens.Bluetooth.DeviceItemsScreenDestination
import com.example.mazdacompanionapp.ui.screens.Bluetooth.EditBluetoothItem
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.AddNewEventScreen
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.EventAddDestination
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.MainEventScreen
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.MainEventScreenDestination


@Composable
fun EventNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = MainEventScreenDestination.route,
            modifier = modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) }
        ) {
            composable(MainEventScreenDestination.route) {
                MainEventScreen(
                    navController = navController,
                    navigateToEventAdd = { navController.navigate(EventAddDestination.route) }
                )
            }
            composable(EventAddDestination.route) {
                AddNewEventScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable(DeviceItemsScreenDestination.route) {
                DeviceItemsScreen(
                    navController,
                    navigateToDeviceAdd = { navController.navigate(DeviceItemAddScreenDestination.route) },
                    navigateToDeviceEdit = { navController.navigate("${DeviceItemEditScreenDestination.route}/$it") }
                )
            }
            composable(DeviceItemAddScreenDestination.route) {
                DeviceItemAddScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable(
                route = DeviceItemEditScreenDestination.routeWithArgs,
                arguments = listOf(navArgument(
                    DeviceItemEditScreenDestination.deviceIdArg
                ) {
                    type = NavType.IntType
                })
            ) {
                EditBluetoothItem(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}