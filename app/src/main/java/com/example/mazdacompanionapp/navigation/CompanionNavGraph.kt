package com.example.mazdacompanionapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mazdacompanionapp.ui.EventaddDestination
import com.example.mazdacompanionapp.ui.HomeDestination
import com.example.mazdacompanionapp.ui.HomeScreen

@Composable
fun CompanionNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(EventaddDestination.route) },
            )
        }
    }
}