package com.example.vibe.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.vibe.data.UserPreferences
import com.example.vibe.model.Event
import com.example.vibe.ui.components.EventDetailsCard
import com.example.vibe.ui.viewmodel.EventsUiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(
    eventsUiState: EventsUiState,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    val isDarkMode by UserPreferences.getDarkModeFlow(context).collectAsState(initial = false) // ✅ Observe saved theme

    val mapProperties by remember(isDarkMode) {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = if (isDarkMode == true) { // ✅ Explicitly check for `true`
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_night)
                } else {
                    null // Default Google Maps style
                }
            )
        )
    }

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

            val cameraPositionState = rememberCameraPositionState()

            LaunchedEffect(events) {
                if (events.isNotEmpty()) {
                    if (events.size == 1) {
                        // If only one event, set a reasonable zoom level (e.g., 13f)
                        val singleEvent = events.first()
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(LatLng(singleEvent.latitude, singleEvent.longitude), 13f)
                        )
                    } else {
                        // Multiple events: Fit all markers within bounds
                        val boundsBuilder = LatLngBounds.builder()
                        events.forEach { event ->
                            boundsBuilder.include(LatLng(event.latitude, event.longitude))
                        }

                        val bounds = boundsBuilder.build()
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200) // 100px padding
                        cameraPositionState.move(cameraUpdate)
                    }
                } else {
                    // Default to Medellín if no events found
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(LatLng(6.2442, -75.5812), 13f)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 104.dp, bottom = 104.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GoogleMap(
                        properties = mapProperties,
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        events.forEach { event ->
                            if (event.latitude != 0.0 && event.longitude != 0.0) { // Ensure valid coordinates
                                val eventLocation = LatLng(event.latitude, event.longitude)

                                val markerColor = when (event.partyMod) {
                                    "House Party" -> BitmapDescriptorFactory.HUE_BLUE
                                    "Pool Party" -> BitmapDescriptorFactory.HUE_AZURE
                                    "Finca Party" -> BitmapDescriptorFactory.HUE_VIOLET
                                    else -> BitmapDescriptorFactory.HUE_RED
                                }

                                Marker(
                                    state = MarkerState(position = eventLocation),
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

                // ✅ Back Button (Unchanged)
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

                // ✅ Event Details Popup (Unchanged)
                selectedEvent?.let { event ->
                    var isPressed by remember { mutableStateOf(false) }

                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 60.dp, 12.dp)
                            .align(Alignment.BottomCenter)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .shadow(10.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isPressed = true
                                        tryAwaitRelease()
                                        isPressed = false
                                        navController.navigate("event_details/${event.id}")
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
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

