package com.example.mazdacompanionapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mazdacompanionapp.screens.AddNewEventScreen
import com.example.mazdacompanionapp.screens.EventAddDestination
import com.example.mazdacompanionapp.screens.MainEventScreen
import com.example.mazdacompanionapp.screens.MainEventScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("APP") },
                navigationIcon = {
                    IconButton(onClick = {


                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }

            )
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = MainEventScreenDestination.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(MainEventScreenDestination.route) {
                MainEventScreen(navigateToEventAdd ={ navController.navigate(EventAddDestination.route) })
            }
            composable(EventAddDestination.route) {
                AddNewEventScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}