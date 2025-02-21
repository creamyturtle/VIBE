package com.example.vibe.ui

import android.content.Context
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
import com.example.vibe.ui.screens.ControlPanelScreen
import com.example.vibe.ui.screens.ErrorScreen
import com.example.vibe.ui.screens.EventCalendarScreen
import com.example.vibe.ui.screens.EventCreationForm
import com.example.vibe.ui.screens.EventDetailsScreen
import com.example.vibe.ui.screens.FAQScreen
import com.example.vibe.ui.screens.HomeScreen
import com.example.vibe.ui.screens.LoadingScreen
import com.example.vibe.ui.screens.LoginScreen
import com.example.vibe.ui.screens.MapScreen
import com.example.vibe.ui.screens.PrivacyPolicyScreen
import com.example.vibe.ui.screens.ReservationScreen
import com.example.vibe.ui.screens.SignupScreen
import com.example.vibe.ui.screens.TermsAndConditionsScreen
import com.example.vibe.ui.screens.UserProfileScreen
import com.example.vibe.ui.screens.geocodeAddress
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.EventsUiState
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.ui.viewmodel.LanguageViewModel
import com.example.vibe.ui.viewmodel.RSVPViewModel
import com.example.vibe.ui.viewmodel.UserViewModel
import com.example.vibe.utils.SessionManager

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
    userViewModel: UserViewModel,
    context: Context,
    languageViewModel: LanguageViewModel
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
                if (filterType != eventsViewModel.lastFilter) {
                    if (filterType == "all") {
                        eventsViewModel.lastFilter = "all" // ✅ Reset lastFilter so filters work again
                        eventsViewModel.getEvents()
                    } else {
                        eventsViewModel.getEventsByType(filterType)
                    }

                    // ✅ Reset scroll position only when switching filters
                    listState.animateScrollToItem(0)
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
                onBack = { navController.navigateUp() },
                context = context
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
                navController = navController,
                eventsViewModel = eventsViewModel,
                userViewModel = userViewModel,
                context = context
            )
        }


        composable(route = "about_us") {

            AboutUsScreen(
                onBack = { navController.navigateUp() }
            )

        }


        composable(route = "terms_and_conditions") {

            TermsAndConditionsScreen(
                onBack = { navController.navigateUp() }
            )

        }


        composable(route = "privacy_policy") {

            PrivacyPolicyScreen(
                onBack = { navController.navigateUp() }
            )

        }


        composable(route = "faq") {

            FAQScreen(
                onBack = { navController.navigateUp() }
            )

        }


        composable(route = "user_profile") {

            UserProfileScreen(userViewModel, SessionManager(context), navController) {
                navController.navigate("login") {
                    popUpTo("user_profile") { inclusive = true } // Clear back stack
                }
            }

        }


        composable(route = "calendar") { backStackEntry ->

            val filterType = backStackEntry.arguments?.getString("filterType") ?: "all"

            LaunchedEffect(filterType) {
                if (filterType != eventsViewModel.lastFilter) {
                    if (filterType == "all") {
                        eventsViewModel.lastFilter = "all"
                        eventsViewModel.getEvents()
                    } else {
                        eventsViewModel.getEventsByType(filterType)
                    }
                }
            }

            when (val state = eventsViewModel.eventsUiState) {
                is EventsUiState.Success -> {
                    EventCalendarScreen(
                        events = state.events,
                        navController = navController,
                        onBack = { navController.navigateUp() }
                    )
                }
                is EventsUiState.Loading -> LoadingScreen()
                else -> ErrorScreen(retryAction = { eventsViewModel.getEvents() })
            }
        }


        composable(route = "control_panel") {

            ControlPanelScreen(
                onBack = { navController.navigateUp() }
            )

        }









    }
}
