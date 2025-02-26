package com.example.vibe.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AppContainer
import com.example.vibe.data.QRCodeApi
import com.example.vibe.network.RSVPApiService
import com.example.vibe.network.RSVPItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class QRViewModel(private val appContainer: AppContainer) : ViewModel() {

    private val _qrScanResult = MutableStateFlow<String?>(null)
    val qrScanResult = _qrScanResult.asStateFlow()

    fun updateScannedQRCode(qrCode: String) {
        _qrScanResult.value = qrCode
    }
}




class CheckInViewModel(private val apiService: RSVPApiService) : ViewModel() {
    var rsvpList by mutableStateOf<List<RSVPItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchApprovedRSVPs()
    }

    fun fetchApprovedRSVPs() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.getApprovedRSVPs()
                if (response.success) {
                    rsvpList = response.rsvps ?: emptyList() // ✅ Ensure it's always a list
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


    fun markUserCheckedIn(qrCode: String) {
        viewModelScope.launch {
            try {
                val matchingRSVP = rsvpList.find { it.qrcode == qrCode }

                if (matchingRSVP == null) {
                    errorMessage = "QR Code not found in the guest list!"
                    return@launch
                }

                val response = apiService.processQRCode(qrCode, matchingRSVP.partyId.toString())

                if (response.success) { // ✅ Use `success` instead of `status`
                    // ✅ Update the guest as checked-in
                    rsvpList = rsvpList.map {
                        if (it.id == matchingRSVP.id) it.copy(enteredparty = 1) else it
                    }
                    successMessage = "User ${matchingRSVP.name} Checked-In Successfully 🎉"
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            }
        }
    }


    fun clearSuccessMessage() {
        successMessage = null
    }
}


