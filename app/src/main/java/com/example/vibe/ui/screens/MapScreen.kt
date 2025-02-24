package com.example.vibe.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.ui.components.EventDetailsCard
import com.example.vibe.ui.viewmodel.EventsUiState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(
    eventsUiState: EventsUiState,
    geocodeAddress: suspend (Context, String) -> LatLng?,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    val isDarkTheme = isSystemInDarkTheme()

    // Map style
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = if (isDarkTheme) {
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_night)
                } else {
                    null // Default Google Maps style
                }
            )
        )
    }

    when (eventsUiState) {
        is EventsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is EventsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Failed to load events", color = Color.Red)
            }
        }

        is EventsUiState.Success -> {
            val events = eventsUiState.events

            // Cache geocoded locations
            val geocodedLocations = remember { mutableStateMapOf<String, LatLng>() }

            // Camera position (DOES NOT RECREATE)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(6.2442, -75.5812), 13f)
            }

            // Perform geocoding asynchronously only when events update
            LaunchedEffect(events) {
                events.forEach { event ->
                    if (event.location.isNotEmpty() && !geocodedLocations.containsKey(event.location)) {
                        val coordinates = geocodeAddress(context, event.location)
                        coordinates?.let { geocodedLocations[event.location] = it }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(top = 104.dp, bottom = 104.dp)) {

                // ✅ Keep GoogleMap stable (prevents full reloading)
                GoogleMap(
                    properties = mapProperties,
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // ✅ Key ensures markers update ONLY when events change
                    key(events) {
                        events.forEach { event ->
                            val coordinates = geocodedLocations[event.location]
                            coordinates?.let { location ->
                                val markerColor = when (event.partyMod) {
                                    "House Party" -> BitmapDescriptorFactory.HUE_BLUE
                                    "Pool Party" -> BitmapDescriptorFactory.HUE_AZURE
                                    "Finca Party" -> BitmapDescriptorFactory.HUE_VIOLET
                                    else -> BitmapDescriptorFactory.HUE_RED
                                }

                                Marker(
                                    state = rememberMarkerState(position = location),
                                    title = event.partyname,
                                    icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                                    onClick = {
                                        selectedEvent = event
                                        true
                                    }
                                )
                            }
                        }
                    }
                }

                // ✅ Back Button
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // ✅ Show event details when a marker is clicked
                selectedEvent?.let { event ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 60.dp, 12.dp)
                            .align(Alignment.BottomCenter)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .shadow(10.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { navController.navigate("event_details/${event.id}") }
                    ) {
                        EventDetailsCard(
                            event,
                            onClose = { selectedEvent = null }
                        )
                    }
                }
            }
        }

        else -> ErrorScreen(retryAction, modifier)
    }
}


