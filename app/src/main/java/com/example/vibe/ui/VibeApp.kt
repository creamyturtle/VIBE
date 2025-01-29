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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vibe.R
import com.example.vibe.ui.screens.EventDetailsScreen
import com.example.vibe.ui.screens.EventsViewModel
import com.example.vibe.ui.screens.HomeScreen
import com.example.vibe.ui.screens.MapScreen
import com.example.vibe.ui.screens.geocodeAddress
import com.google.android.gms.maps.model.LatLng
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


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { VibeTopAppBar(navController) },
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
            }

            /*
            else {
                ReservationsBottomBar(navController = navController)
            }
            */

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

                    val eventsViewModel: EventsViewModel = viewModel(factory = EventsViewModel.Factory)

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

                    val eventsViewModel: EventsViewModel = viewModel(factory = EventsViewModel.Factory)

                    LaunchedEffect(filterType) {
                        if (filterType == "all") {
                            eventsViewModel.getEvents() // Fetch all events
                        } else {
                            eventsViewModel.getEventsByType(filterType) // Fetch filtered events
                        }
                    }

                    MapScreen(
                        eventsUiState = eventsViewModel.eventsUiState,
                        geocodeAddress = { context, address -> geocodeAddress(context, address) }
                    )
                }


                composable(
                    route = "event_details/{eventId}",
                    arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                ) { backStackEntry ->

                    val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                    val eventsViewModel: EventsViewModel =
                        viewModel(factory = EventsViewModel.Factory)

                    EventDetailsScreen(
                        contentPadding = innerPadding,
                        event = eventsViewModel.selectedEvent,
                        onBack = { navController.navigateUp() }
                    )
                    eventsViewModel.selectEvent(eventId)
                }
            }



    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibeTopAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    //.background(Color.Red)
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp, 0.dp, 8.dp),
                    //.offset(x = (-16).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {


                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                    //modifier = Modifier
                        //.background(Color.Green)
                ) {

                    FancyAnimatedButton(
                        onClick = {
                            navController.navigate("home_screen/all")
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_white),
                            contentDescription = "VIBE Logo",
                            modifier = Modifier
                                .padding(0.dp, 12.dp, 0.dp, 0.dp)
                                //.offset(x = 8.dp)
                                //.align(Alignment.Top)
                                .width(143.dp) // Use the image's exact width
                                .height(57.dp) // Use the image's exact height
                        )
                    }

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = "Your official party connection",
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp, 0.dp)
                    )



                }


                Spacer(
                    Modifier
                    .size(24.dp)
                )


                Column(
                    //modifier = Modifier
                        //.background(Color.Blue),
                        //.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {

                    HamburgerMenuButton()

                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 24.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Log In",
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = "Log In",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                        }

                        Spacer(Modifier.width(16.dp))


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Groups,
                                contentDescription = "Host an Event",
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = "Host Event",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                        }

                        Spacer(Modifier.width(12.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Event,
                                contentDescription = "Calendar",
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = "Calendar",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                        }


                    }


                }



            }
        },
        modifier = Modifier
            .fillMaxHeight(.18f),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF333F57),
            titleContentColor = Color.White
        )
    )
}


@Composable
fun VibeBottomAppBar(navController: NavController, offsetY: Dp) {

    BottomAppBar (
        modifier = Modifier
            //.offset(y = offsetY)
            .height(148.dp - offsetY),
        containerColor = Color(0xFF333F57), // Background color for the BottomAppBar
        contentColor = Color.White         // Default content (icon/text) color
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .offset(x = (-4).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/House") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.house),
                        contentDescription = "House Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = "House Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = {
                    navController.navigate("home_screen/Finca") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }
                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.finca),
                        contentDescription = "Finca Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Finca Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }

            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/Pool") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.pool),
                        contentDescription = "Pool Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Pool Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }

            }

            Spacer(Modifier.width(8.dp))

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/Activity") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.activities),
                        contentDescription = "Activities",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Activities",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }
            }


        }


    }
}



@Composable
fun MapBottomAppBar(
    navController: NavController,
    onFilterSelected: (String) -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF333F57),
        contentColor = Color.White,
        modifier = Modifier
            .height(148.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FancyAnimatedButton(
                onClick = { onFilterSelected("House") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.house),
                        contentDescription = "House Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "House Parties", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            FancyAnimatedButton(
                onClick = { onFilterSelected("Finca") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.finca),
                        contentDescription = "Finca Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "Finca Parties", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            FancyAnimatedButton(
                onClick = { onFilterSelected("Pool") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.pool),
                        contentDescription = "Pool Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "Pool Parties", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            FancyAnimatedButton(
                onClick = { onFilterSelected("Activity") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.activities),
                        contentDescription = "Activities",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "Activities", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}






@Composable
fun ReservationsBottomBar(navController: NavController) {

    BottomAppBar (
        modifier = Modifier
            //.offset(y = offsetY)
            .height(148.dp),
        containerColor = Color(0xFF333F57), // Background color for the BottomAppBar
        contentColor = Color.White         // Default content (icon/text) color
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .offset(x = (-4).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/House") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.house),
                        contentDescription = "House Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = "House Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = {
                    navController.navigate("home_screen/Finca") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }
                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.finca),
                        contentDescription = "Finca Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Finca Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }

            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/Pool") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.pool),
                        contentDescription = "Pool Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Pool Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }

            }

            Spacer(Modifier.width(8.dp))

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/Activity") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.activities),
                        contentDescription = "Activities",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Activities",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }
            }


        }


    }
}



@Composable
fun HamburgerMenuButton() {
    var menuExpanded by remember { mutableStateOf(false) } // Tracks if the menu is open

    Box {
        // Combine Text and Icon into a Row
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { menuExpanded = true } // Make the entire Row clickable
                .padding(8.dp, 8.dp, 16.dp, 8.dp), // Optional padding
            verticalAlignment = Alignment.CenterVertically // Align text and icon vertically
        ) {
            Text(
                text = "Menu",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFE1943),
                modifier = Modifier.padding(end = 4.dp) // Space between text and icon
            )

            Spacer(Modifier.size(8.dp))

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu Button",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Dropdown menu
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }, // Close the menu when dismissed
            properties = PopupProperties(focusable = true)
        ) {
            // Menu options
            DropdownMenuItem(
                text = { Text("Option 1") },
                onClick = {
                    menuExpanded = false
                    // Handle Option 1 click
                }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = {
                    menuExpanded = false
                    // Handle Option 2 click
                }
            )
            DropdownMenuItem(
                text = { Text("Option 3") },
                onClick = {
                    menuExpanded = false
                    // Handle Option 3 click
                }
            )
            DropdownMenuItem(
                text = { Text("Option 4") },
                onClick = {
                    menuExpanded = false
                    // Handle Option 4 click
                }
            )
            DropdownMenuItem(
                text = { Text("Option 5") },
                onClick = {
                    menuExpanded = false
                    // Handle Option 5 click
                }
            )
        }
    }
}



@Composable
fun FancyAnimatedButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    // Scale Animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f, // Shrink slightly when pressed
        animationSpec = tween(durationMillis = 150) // Fast animation
    )

    // Ripple Effect
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true // Start press animations
                        try {
                            awaitRelease() // Wait for user to release
                        } finally {
                            isPressed = false // Reset press state
                        }
                        onClick() // Trigger onClick action
                    }
                )
            }
            .indication(interactionSource, LocalIndication.current), // Default ripple effect
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


