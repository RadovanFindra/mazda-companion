package com.example.mazdacompanionapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mazdacompanionapp.ui.Navigation.CompanionNavHost

@Composable
fun CompanionApp(navController: NavHostController = rememberNavController()) {
    CompanionNavHost(navController = navController)
}