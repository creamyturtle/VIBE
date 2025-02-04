package com.example.vibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vibe.data.AuthRepository
import com.example.vibe.network.SignupApi
import com.example.vibe.ui.screens.EventDetailsScreen
import com.example.vibe.ui.screens.EventsViewModel
import com.example.vibe.ui.screens.HomeScreen
import com.example.vibe.ui.screens.LoginScreen
import com.example.vibe.ui.screens.MapScreen
import com.example.vibe.ui.screens.SignupScreen
import com.example.vibe.ui.screens.geocodeAddress

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VibeNavHost(
    navController: NavHostController,
    listState: LazyListState,
    innerPadding: PaddingValues,
    eventsViewModel: EventsViewModel,
    authRepository: AuthRepository,
    signupApi: SignupApi,
    animatedOffset: Dp
) {
    NavHost(
        navController = navController,
        startDestination = "home_screen/{filterType}",
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            route = "home_screen/{filterType}",
            arguments = listOf(navArgument("filterType") { type = NavType.StringType })
        ) { backStackEntry ->

            val filterType = backStackEntry.arguments?.getString("filterType") ?: "all"

            // Fetch events based on the filter type
            LaunchedEffect(filterType) {
                if (filterType == "all") {
                    eventsViewModel.getEvents() // Fetch all events
                } else {
                    eventsViewModel.getEventsByType(filterType) // Fetch filtered events
                }
            }

            HomeScreen(
                listState = listState,
                eventsUiState = eventsViewModel.eventsUiState,
                contentPadding = innerPadding,
                retryAction = eventsViewModel::getEvents,
                onEventClick = { eventId ->
                    navController.navigate("event_details/$eventId")
                },
                navController = navController,
                offsetY = animatedOffset
            )
        }


        composable(
            route = "map_screen/{filterType}",
            arguments = listOf(navArgument("filterType") { type = NavType.StringType })
        ) { backStackEntry ->

            val filterType = backStackEntry.arguments?.getString("filterType") ?: "all"


            LaunchedEffect(filterType) {
                if (filterType == "all") {
                    eventsViewModel.getEvents() // Fetch all events
                } else {
                    eventsViewModel.getEventsByType(filterType) // Fetch filtered events
                }
            }

            MapScreen(
                eventsUiState = eventsViewModel.eventsUiState,
                geocodeAddress = { context, address -> geocodeAddress(context, address) },
                navController = navController
            )
        }


        composable(
            route = "event_details/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->

            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""

            EventDetailsScreen(
                contentPadding = innerPadding,
                event = eventsViewModel.selectedEvent,
                onBack = { navController.navigateUp() }
            )
            eventsViewModel.selectEvent(eventId)
        }


        composable(route = "login") {

            LoginScreen(
                navController = navController,
                authRepository = authRepository,
                onLoginSuccess = { navController.popBackStack() }, // Navigates back after login
                onBack = { navController.navigateUp() }
            )
        }


        composable(route = "signup") {

            SignupScreen(
                navController = navController,
                signupApi = signupApi,
                onSignupSuccess = { navController.popBackStack() }, // Navigates back after login
                onBack = { navController.navigateUp() }
            )
        }
    }
}
