package com.example.vibe.data

import android.os.Bundle
import android.location.Geocoder
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = this
            var coordinates by remember { mutableStateOf<LatLng?>(null) }
            val scope = rememberCoroutineScope()

            // Fetch coordinates for the address
            LaunchedEffect(Unit) {
                scope.launch {
                    coordinates = geocodeAddress(context, "1600 Amphitheatre Parkway, Mountain View, CA")
                }
            }

            // Initialize Camera Position State
            val cameraPositionState = rememberCameraPositionState {
                coordinates?.let {
                    position = CameraPosition.Builder()
                        .target(it) // Center on the fetched location
                        .zoom(15f)  // Set zoom level
                        .build()
                }
            }

            // Google Map Composable
            GoogleMap(cameraPositionState = cameraPositionState) {
                coordinates?.let {
                    Marker(
                        state = MarkerState(position = it), // Correct: MarkerState encapsulates position
                        title = "Google HQ"
                    )
                }
            }
        }
    }

    // Suspend function for Geocoding
    private suspend fun geocodeAddress(context: Context, address: String): LatLng? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context)
                val results = geocoder.getFromLocationName(address, 1)
                if (results != null && results.isNotEmpty()) {
                    LatLng(results[0].latitude, results[0].longitude) // Proper LatLng creation
                } else {
                    null // Handle no results gracefully
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
