package com.example.vibe.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TopBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    val noTopBarScreens = setOf("login", "signup", "reservation_screen", "host_event") // Add base route

    if (currentDestination !in noTopBarScreens &&
        currentDestination?.startsWith("event_details") == false &&
        currentDestination?.startsWith("reservation_screen") == false) { // Check prefix for dynamic routes
        VibeTopAppBar(navController)
    }
}

