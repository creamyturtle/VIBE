package com.example.vibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vibe.network.SignupApi
import com.example.vibe.ui.screens.AboutUsScreen
import com.example.vibe.ui.screens.EventCreationForm
import com.example.vibe.ui.screens.EventDetailsScreen
import com.example.vibe.ui.screens.HomeScreen
import com.example.vibe.ui.screens.LoginScreen
import com.example.vibe.ui.screens.MapScreen
import com.example.vibe.ui.screens.PrivacyPolicyScreen
import com.example.vibe.ui.screens.ReservationScreen
import com.example.vibe.ui.screens.SignupScreen
import com.example.vibe.ui.screens.TermsAndConditionsScreen
import com.example.vibe.ui.screens.geocodeAddress
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.ui.viewmodel.RSVPViewModel
import com.example.vibe.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VibeNavHost(
    navController: NavHostController,
    listState: LazyListState,
    innerPadding: PaddingValues,
    eventsViewModel: EventsViewModel,
    signupApi: SignupApi,
    authViewModel: AuthViewModel,
    rsvpViewModel: RSVPViewModel,
    userViewModel: UserViewModel
) {

    //val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

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
                navController = navController
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

            val event = eventsViewModel.selectedEvent // ✅ Get event from ViewModel
            LaunchedEffect(eventId) {
                eventsViewModel.selectEvent(eventId) // ✅ Fetch the selected event
            }

            EventDetailsScreen(
                contentPadding = innerPadding,
                event = event,
                onBack = { navController.navigateUp() }
            )

        }


        composable(
            route = "reservation_screen/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""

            val event = eventsViewModel.selectedEvent // ✅ Retrieve the event from ViewModel
            LaunchedEffect(eventId) {
                eventsViewModel.selectEvent(eventId) // ✅ Load the correct event
            }

            ReservationScreen(
                event = event,
                onBack = { navController.navigateUp() },
                authViewModel = authViewModel,
                rsvpViewModel = rsvpViewModel,
                userViewModel = userViewModel,
                navController = navController
            )
        }



        composable(route = "login") {

            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                onBack = { navController.navigateUp() }
            )
        }


        composable(route = "signup") {

            SignupScreen(
                navController = navController,
                signupApi = signupApi,
                onSignupSuccess = { navController.popBackStack() }
            )
        }

        composable(route = "host_event") {

            EventCreationForm(
                navController = navController
            )
        }


        composable(route = "about_us") {

            AboutUsScreen(
                navController = navController,
                onBack = { navController.navigateUp() }
            )

        }


        composable(route = "terms_and_conditions") {

            TermsAndConditionsScreen(
                navController = navController,
                onBack = { navController.navigateUp() }
            )

        }


        composable(route = "privacy_policy") {

            PrivacyPolicyScreen(
                navController = navController,
                onBack = { navController.navigateUp() }
            )

        }





    }
}
