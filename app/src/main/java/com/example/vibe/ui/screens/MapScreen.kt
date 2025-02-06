package com.example.vibe.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.ui.viewmodel.EventsUiState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(
    eventsUiState: EventsUiState,
    geocodeAddress: suspend (Context, String) -> LatLng?,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    when (eventsUiState) {
        is EventsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is EventsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Failed to load events", color = Color.Red)
            }
        }
        is EventsUiState.Success -> {
            val events = eventsUiState.events

            // Cache for geocoded results
            val geocodedLocations = remember { mutableStateMapOf<String, LatLng>() }


            // Get the initial camera position
            val initialPosition = geocodedLocations.values.firstOrNull() ?: LatLng(6.2442, -75.5812)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(initialPosition, 13f)
            }

            // Perform geocoding for all event locations
            LaunchedEffect(events) {
                events.forEach { event ->
                    if (event.location.isNotEmpty() && !geocodedLocations.containsKey(event.location)) {
                        val coordinates = geocodeAddress(context, event.location) // Pass both Context and address
                        coordinates?.let { geocodedLocations[event.location] = it }
                    }
                }
            }

            Box(
                modifier = Modifier
                .fillMaxSize()
                .padding(top = 168.dp, bottom = 148.dp)
            ) {


                Box(modifier = Modifier.fillMaxSize()) {
                    GoogleMap(
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier
                            .fillMaxSize()

                    ) {

                        events.forEach { event ->

                            //val icon = bitmapDescriptorFromComposable { MarkerBubble(event.partyname) }
                            val coordinates = geocodedLocations[event.location]

                            coordinates?.let { location ->

                                val markerColor = when (event.partyMod) {
                                    "House Party" -> BitmapDescriptorFactory.HUE_BLUE
                                    "Pool Party" -> BitmapDescriptorFactory.HUE_AZURE
                                    "Finca Party" -> BitmapDescriptorFactory.HUE_VIOLET
                                    else -> BitmapDescriptorFactory.HUE_RED // Default
                                }

                                Marker(
                                    state = MarkerState(position = location),
                                    title = event.partyname,
                                    icon = BitmapDescriptorFactory.defaultMarker(markerColor), // Set color dynamically
                                    onClick = {
                                        selectedEvent = event
                                        true
                                    }
                                )
                            }
                        }
                    }



                }

                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(color = Color.White, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize() // Fill the `IconButton` area
                            .padding(0.dp) // Adjust the internal padding here
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(20.dp)
                        )
                    }


                }


                // ✅ Show event details when a marker is clicked
                selectedEvent?.let { event ->

                    var isPressed by remember { mutableStateOf(false) }

                    // ✅ Animate scale effect
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.95f else 1f, // Shrink when pressed
                        animationSpec = tween(durationMillis = 100) // Smooth quick animation
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 60.dp, 12.dp)
                            .align(Alignment.BottomCenter)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            ) // ✅ Apply scale effect to the Box too!
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .shadow(10.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isPressed = true
                                        tryAwaitRelease() // ✅ Wait for user to release
                                        isPressed = false
                                        navController.navigate("event_details/${event.id}") // ✅ Navigate AFTER release
                                    }
                                )
                            }
                    ) {
                        EventDetailsCard(
                            event,
                            onClose = { selectedEvent = null }
                        )
                    }
                }


            }


        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailsCard(event: Event, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            //.shadow(8.dp)
            .padding(16.dp, 4.dp, 16.dp, 16.dp)

    ) {
        Column () {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = event.partyname, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {


                AsyncImage(
                    model = event.fullImgSrc,
                    contentDescription = event.partyname,
                    modifier = Modifier
                        //.height(200.dp)
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.defaultimg)
                )

                Spacer(Modifier.width(12.dp))


                Column() {
                    Text(
                        text = "📍 ${event.location}",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            textIndent = TextIndent(firstLine = 0.sp, restLine = 22.sp)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "🎉 ${event.partyMod}",
                        fontSize = 16.sp,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            textIndent = TextIndent(firstLine = 0.sp, restLine = 22.sp)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "📅 ${event.formattedDate}",
                        fontSize = 16.sp,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            textIndent = TextIndent(firstLine = 0.sp, restLine = 22.sp)
                        )
                    )
                }


            }



        }
    }
}


