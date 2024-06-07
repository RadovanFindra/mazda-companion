package com.example.mazdacompanionapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mazdacompanionapp.ui.Navigation.EventNavHost

@Composable
fun CompanionApp(navController: NavHostController = rememberNavController()) {
    EventNavHost(navController = navController)
}