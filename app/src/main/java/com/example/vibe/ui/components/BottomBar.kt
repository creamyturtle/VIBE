package com.example.vibe.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.ui.viewmodel.LanguageViewModel

@Composable
fun BottomBar(
    navController: NavController,
    isLoggedIn: Boolean,
    eventsViewModel: EventsViewModel,
    authViewModel: AuthViewModel,
    languageViewModel: LanguageViewModel
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    when {
        currentDestination?.startsWith("home_screen") == true -> {
            VibeBottomAppBarNew(navController, isLoggedIn, authViewModel, languageViewModel)
        }
        currentDestination?.startsWith("map_screen") == true -> {
            MapBottomAppBar(navController) { filterType ->
                navController.navigate("map_screen/$filterType") {
                    popUpTo("map_screen/all") { inclusive = true }
                }
            }
        }
        currentDestination?.startsWith("event_details") == true -> {
            val selectedEvent = eventsViewModel.selectedEvent
            selectedEvent?.let {
                ReservationsBottomBar(navController, it) // ✅ Pass event when in `event_details`
            }
        }

    }
}

