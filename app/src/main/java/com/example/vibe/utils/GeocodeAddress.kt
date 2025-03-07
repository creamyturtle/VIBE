package com.example.vibe.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

suspend fun geocodeAddress(context: Context, address: String): LatLng? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            @Suppress("DEPRECATION") // ✅ Suppress the warning until Google provides a replacement
            val results = geocoder.getFromLocationName(address, 1)

            if (!results.isNullOrEmpty()) {
                LatLng(results[0].latitude, results[0].longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Geocoder", "Error geocoding address: ${e.message}")
            null
        }
    }
}
