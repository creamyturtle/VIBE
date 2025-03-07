package com.example.vibe.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vibe.data.AppContainer
import com.example.vibe.network.SignupApi
import com.example.vibe.ui.screens.AboutUsScreen
import com.example.vibe.ui.screens.ApproveReservationsScreen
import com.example.vibe.ui.screens.CheckInScreen
import com.example.vibe.ui.screens.ContactScreen
import com.example.vibe.ui.screens.ControlPanelScreen
import com.example.vibe.ui.screens.ErrorScreen
import com.example.vibe.ui.screens.EventCalendarScreen
import com.example.vibe.ui.screens.EventCreationForm
import com.example.vibe.ui.screens.EventDetailsScreen
import com.example.vibe.ui.screens.EventsAttendingScreen
import com.example.vibe.ui.screens.FAQScreen
import com.example.vibe.ui.screens.HomeScreen
import com.example.vibe.ui.screens.LoadingScreen
import com.example.vibe.ui.screens.LoginScreen
import com.example.vibe.ui.screens.ManageHostedScreen
import com.example.vibe.ui.screens.MapScreen
import com.example.vibe.ui.screens.PrivacyPolicyScreen
import com.example.vibe.ui.screens.ReservationScreen
import com.example.vibe.ui.screens.SignupScreen
import com.example.vibe.ui.screens.TermsAndConditionsScreen
import com.example.vibe.ui.screens.UserProfileScreen
import com.example.vibe.ui.viewmodel.ApproveReservationsViewModel
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.CheckInViewModel
import com.example.vibe.ui.viewmodel.ContactViewModel
import com.example.vibe.ui.viewmodel.EventsUiState
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.ui.viewmodel.QRViewModel
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
    userViewModel: UserViewModel,
    context: Context,
    appContainer: AppContainer,
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            route = "home_screen/{filterType}",
            arguments = listOf(navArgument("filterType") { type = NavType.StringType })
        ) { backStackEntry ->
            val filterType = backStackEntry.arguments?.getString("filterType") ?: "all"


            // ✅ Prevent duplicate API calls if a last search exists
            LaunchedEffect(filterType, eventsViewModel.lastSearchQuery) {
                Log.d("UI", "📢 Filter changed: Fetching events")

                if (eventsViewModel.lastSearchQuery.isNotEmpty()) {
                    Log.d("UI", "🔍 Loading last searched location: ${eventsViewModel.lastSearchQuery}")
                    eventsViewModel.getByLocation(eventsViewModel.lastSearchQuery, filterType)
                } else {
                    if (filterType == "all") {
                        eventsViewModel.getEvents()
                    } else {
                        eventsViewModel.getEventsByType(filterType)
                    }
                }

                // ✅ Reset scroll position only if a filter change occurs
                listState.animateScrollToItem(0)
            }


            HomeScreen(
                listState = listState, // ✅ Retains scroll position unless a filter changes
                eventsUiState = eventsViewModel.eventsUiState,
                contentPadding = innerPadding,
                retryAction = eventsViewModel::getEvents,
                onEventClick = { eventId -> navController.navigate("event_details/$eventId") },
                navController = navController,
                eventsViewModel = eventsViewModel
            )
        }




        composable(
            route = "map_screen/{filterType}",
            arguments = listOf(navArgument("filterType") { type = NavType.StringType })
        ) { backStackEntry ->

            val filterType = backStackEntry.arguments?.getString("filterType") ?: "all"


            LaunchedEffect(filterType) {
                if (eventsViewModel.lastSearchQuery.isNotEmpty()) {
                    eventsViewModel.getByLocation(eventsViewModel.lastSearchQuery, filterType)
                } else {
                    if (filterType == "all") {
                        eventsViewModel.getEvents()
                    } else {
                        eventsViewModel.getEventsByType(filterType)
                    }
                }
            }


            MapScreen(
                eventsUiState = eventsViewModel.eventsUiState,
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


            val rsvpViewModel = remember { RSVPViewModel(appContainer.rsvpApi, appContainer.sessionManager) }

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

            //val filterType = backStackEntry.arguments?.getString("filterType") ?: "all"

            /*
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

             */

            when (val state = eventsViewModel.eventsUiState) {
                is EventsUiState.Success -> {
                    EventCalendarScreen(
                        events = state.events,
                        navController = navController
                    )
                }

                is EventsUiState.Loading -> LoadingScreen()
                else -> ErrorScreen(retryAction = { eventsViewModel.getEvents() })
            }
        }


        composable(route = "control_panel") {

            ControlPanelScreen(
                userViewModel = userViewModel,
                navController = navController
            )

        }


        composable(route = "events_attending") {


            LaunchedEffect(Unit) {
                eventsViewModel.getAttending()
            }



            EventsAttendingScreen(
                eventsUiState = eventsViewModel.eventsUiState,
                retryAction = eventsViewModel::getAttending,
                onCancelReservation = { tableName ->
                    eventsViewModel.cancelReservation(tableName)
                },
                onBack = { navController.navigateUp() }
            )


        }

        composable(route = "manage_hosted") {


            LaunchedEffect(Unit) {
                eventsViewModel.getByID()
            }



            ManageHostedScreen(
                eventsUiState = eventsViewModel.eventsUiState,
                navController = navController,
                retryAction = eventsViewModel::getByID,
                onCancelEvent = { },
                onBack = { navController.navigateUp() }
            )


        }



        composable(route = "approve_reservations") {


            val approveReservationsViewModel = remember { ApproveReservationsViewModel(appContainer.rsvpApiService) }


            LaunchedEffect(Unit) {

                approveReservationsViewModel.fetchPendingRSVPs()
            }



            ApproveReservationsScreen(
                approveReservationsViewModel = approveReservationsViewModel,
                onBack = { navController.navigateUp() }
            )


        }


        composable(route = "check_in") {

            val qrViewModel = remember { QRViewModel(appContainer) }
            val checkInViewModel = remember { CheckInViewModel(appContainer.rsvpApiService) }


            CheckInScreen(
                qrViewModel = qrViewModel,
                checkInViewModel = checkInViewModel,
                onBack = { navController.navigateUp() }
            )

            LaunchedEffect(Unit) {
                checkInViewModel.clearErrorMessage()
            }


        }


        composable(route = "contact") {

            val contactViewModel = remember { ContactViewModel(appContainer.contactApi)}

            ContactScreen(
                navController = navController,
                contactViewModel = contactViewModel
            )

        }


    }

}
