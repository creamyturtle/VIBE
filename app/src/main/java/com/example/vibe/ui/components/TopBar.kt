package com.example.vibe.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TopBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    val noTopBarScreens = setOf("login", "signup") // Screens that don't show a top bar

    if (currentDestination !in noTopBarScreens && currentDestination?.startsWith("event_details") == false) {
        VibeTopAppBar(navController)
    }
}
