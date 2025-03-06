package com.example.vibe.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.network.RSVPApiService
import com.example.vibe.network.RSVPItem
import kotlinx.coroutines.launch

class ApproveReservationsViewModel(private val apiService: RSVPApiService) : ViewModel() {
    var rsvpList by mutableStateOf<List<RSVPItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    //init {
    //    fetchPendingRSVPs()
    //}

    fun fetchPendingRSVPs() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.getPendingRSVPs()
                if (response.success) {
                    rsvpList = response.rsvps!!
                } else {
                    errorMessage = "Failed to fetch RSVPs."
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun approveRSVP(rsvp: RSVPItem) {
        viewModelScope.launch {
            try {
                Log.d("RSVP_APPROVE", "Attempting to approve: ID=${rsvp.id}, Party ID=${rsvp.partyId}")

                val response = apiService.approveRSVP(
                    approveId = rsvp.id,
                    partyId = rsvp.partyId
                )

                Log.d("RSVP_APPROVE_RESPONSE", "Response: $response")

                if (response.success) {
                    rsvpList = rsvpList.filter { it.id != rsvp.id }
                    successMessage = "RSVP Approved Successfully 🎉" // ✅ Show toast message
                    Log.d("RSVP_APPROVE", "✅ RSVP approved successfully!")
                } else {
                    errorMessage = "Approval failed. Server message: ${response.message}"
                    Log.e("RSVP_APPROVE", "❌ Approval failed: ${response.message}")
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                Log.e("RSVP_APPROVE", "❌ Error approving RSVP: ${e.message}")
            }
        }
    }

    // ✅ Helper function to clear success message after showing Toast
    fun clearSuccessMessage() {
        successMessage = null
    }
}

