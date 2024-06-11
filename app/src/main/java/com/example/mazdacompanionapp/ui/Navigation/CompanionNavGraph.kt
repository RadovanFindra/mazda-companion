package com.example.mazdacompanionapp.ui.Navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.AddNewEventScreen
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.EditEvent
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.EventAddDestination
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.EventEditScreenDestination
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.MainEventScreen
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.MainEventScreenDestination
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemAddScreen
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemAddScreenDestination
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemEditScreenDestination
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemsScreen
import com.example.mazdacompanionapp.ui.screens.devices.DeviceItemsScreenDestination
import com.example.mazdacompanionapp.ui.screens.devices.EditBluetoothItem


@Composable
fun CompanionNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = MainEventScreenDestination.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(300)) }
    ) {
        composable(MainEventScreenDestination.route) {
            MainEventScreen(
                navController = navController,
                navigateToEventAdd = { navController.navigate(EventAddDestination.route) },
                navigateToEventEdit = { navController.navigate("${EventEditScreenDestination.route}/$it") }
            )
        }
        composable(EventAddDestination.route) {
            AddNewEventScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = EventEditScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(
                EventEditScreenDestination.eventIdArg
            ) {
                type = NavType.IntType
            })
        ) {
            EditEvent(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
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
