/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vibe.data.AuthRepository
import com.example.vibe.data.DefaultAppContainer
import com.example.vibe.ui.components.MapBottomAppBar
import com.example.vibe.ui.components.ReservationsBottomBar
import com.example.vibe.ui.components.VibeBottomAppBar
import com.example.vibe.ui.components.VibeTopAppBar
import com.example.vibe.ui.screens.EventDetailsScreen
import com.example.vibe.ui.screens.EventsViewModel
import com.example.vibe.ui.screens.HomeScreen
import com.example.vibe.ui.screens.LoginScreen
import com.example.vibe.ui.screens.MapScreen
import com.example.vibe.ui.screens.SignupScreen
import com.example.vibe.ui.screens.geocodeAddress
import com.example.vibe.utils.SessionManager
import kotlin.math.max
import kotlin.math.min


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VibeApp() {

    val listState = rememberLazyListState()

        var appBarOffset by remember { mutableStateOf(0f) }
        var previousOffset by remember { mutableStateOf(0) }

        LaunchedEffect(listState.firstVisibleItemScrollOffset, listState.firstVisibleItemIndex) {
            val currentOffset = listState.firstVisibleItemScrollOffset
            val scrollDelta = currentOffset - previousOffset

            // Update the appBarOffset based on scroll direction
            appBarOffset = when {
                scrollDelta > 0 -> min(148f, appBarOffset + scrollDelta) // Scrolling down
                scrollDelta < 0 -> max(0f, appBarOffset + scrollDelta)  // Scrolling up
                else -> appBarOffset
            }

            // Update the previous offset for the next comparison
            previousOffset = currentOffset
        }

        val animatedOffset by animateDpAsState(
            targetValue = appBarOffset.dp,
            animationSpec = tween(durationMillis = 600) // Smooth animation
        )


    val navController = rememberNavController()

    val context = LocalContext.current

    val appContainer = remember { DefaultAppContainer(context) } // ✅ Initialize AppContainer
    val authRepository = remember { AuthRepository(appContainer.authApi, SessionManager(context)) }

    val eventsViewModel: EventsViewModel = viewModel(factory = EventsViewModel.Factory)


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

            val noTopBarScreens = setOf("login", "signup") // Screens that don't show a top bar

            if (currentDestination !in noTopBarScreens && currentDestination?.startsWith("event_details") == false) {
                VibeTopAppBar(navController)
            }

        },
        bottomBar = {

            val currentBackStackEntry by navController.currentBackStackEntryAsState()

            val currentDestination = currentBackStackEntry?.destination?.route

            when {
                currentDestination?.startsWith("home_screen") == true -> {
                    VibeBottomAppBar(navController, animatedOffset)
                }
                currentDestination?.startsWith("map_screen") == true -> {
                    MapBottomAppBar(navController) { filterType ->
                        navController.navigate("map_screen/$filterType") {
                            popUpTo("map_screen/all") { inclusive = true }
                        }
                    }
                }
                currentDestination?.startsWith("event_details") == true -> {
                    ReservationsBottomBar(navController = navController)
                }


            }




        }

    ) { innerPadding ->



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
                        signupApi = appContainer.signupApi,
                        onSignupSuccess = { navController.popBackStack() }, // Navigates back after login
                        onBack = { navController.navigateUp() }
                    )
                }




            }



    }
}







