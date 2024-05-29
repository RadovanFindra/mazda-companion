package com.example.mazdacompanionapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.mazdacompanionapp.screens.AddNewEventScreen
import com.example.mazdacompanionapp.screens.EventAddDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun EventNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
            NavHost(
                navController,
                startDestination = MainEventScreenDestination.route) {
//                composable(MainEventScreenDestination.route) {
//                    MainEventScreen(
//                        onAddEventClick = { navController.navigate(EventAddDestination.route) },
//                        events = events,
//                        onEventClick = { /* handle event click if needed */ }
//                    )
//                }
                composable(EventAddDestination.route) {
                    AddNewEventScreen(

                    )
                }
            }
}