package com.example.vibe.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    eventsUiState: EventsUiState,
    geocodeAddress: suspend (Context, String) -> LatLng?
) {
    val context = LocalContext.current

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

            // Perform geocoding for all event locations
            LaunchedEffect(events) {
                events.forEach { event ->
                    if (event.location.isNotEmpty() && !geocodedLocations.containsKey(event.location)) {
                        val coordinates = geocodeAddress(context, event.location) // Pass both Context and address
                        coordinates?.let { geocodedLocations[event.location] = it }
                    }
                }
            }

            // Get the initial camera position
            val initialPosition = geocodedLocations.values.firstOrNull() ?: LatLng(6.2442, -75.5812)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(initialPosition, 12f)
            }


            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 148.dp)
            ) {
                // Add markers for geocoded locations
                events.forEach { event ->
                    val coordinates = geocodedLocations[event.location]
                    coordinates?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = event.partyname,
                            snippet = event.description
                        )
                    }
                }
            }
        }
    }
}
