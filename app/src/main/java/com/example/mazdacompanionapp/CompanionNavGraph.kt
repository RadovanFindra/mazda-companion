package com.example.mazdacompanionapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mazdacompanionapp.screens.AddNewEventScreen
import com.example.mazdacompanionapp.screens.EventAddDestination
import com.example.mazdacompanionapp.screens.MainEventScreen
import com.example.mazdacompanionapp.screens.MainEventScreenDestination

@Composable
fun EventNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
            NavHost(
                navController,
                startDestination = MainEventScreenDestination.route,
                modifier = modifier
            ) {
                composable(MainEventScreenDestination.route) {
                    MainEventScreen(
                       navigateToEventAdd = { navController.navigate(EventAddDestination.route)}
                    )
                }
                composable(EventAddDestination.route) {
                    AddNewEventScreen(
                        navigateBack = { navController.popBackStack() },
                        onNavigateUp = { navController.navigateUp() }
                    )
                }

            }
}