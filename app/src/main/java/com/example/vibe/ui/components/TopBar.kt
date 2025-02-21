package com.example.vibe.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TopBar(navController: NavController, isDrawerOpen: MutableState<Boolean>, listState: LazyListState) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    when {
        // Screens that should have a basic top bar
        currentDestination in setOf("about_us", "faq", "terms_and_conditions", "privacy_policy", "user_profile") ||
                currentDestination?.startsWith("map_screen") == true -> {
            VibeBasicTopBar(navController, isDrawerOpen)
        }

        // Screens that should have no top bar at all
        currentDestination in setOf("login", "signup", "reservation_screen", "host_event") ||
                currentDestination?.startsWith("event_details") == true ||
                currentDestination?.startsWith("reservation_screen") == true -> {
            // Do nothing (No Top Bar)
        }

        // Default case: Show the main top bar
        else -> {
            VibeTopAppBar(navController, isDrawerOpen, listState)
        }
    }
}


