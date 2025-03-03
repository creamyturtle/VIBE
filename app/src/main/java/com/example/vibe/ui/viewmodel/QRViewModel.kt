package com.example.vibe.ui.viewmodel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AppContainer
import com.example.vibe.network.RSVPApiService
import com.example.vibe.network.RSVPItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class QRViewModel(private val appContainer: AppContainer) : ViewModel() {

    private val _qrScanResult = MutableStateFlow<String?>(null)
    val qrScanResult = _qrScanResult.asStateFlow()

    private val _isScanning = MutableStateFlow(false) // ✅ Track scanning state in ViewModel
    val isScanning = _isScanning.asStateFlow()

    fun updateScannedQRCode(qrCode: String) {
        _qrScanResult.value = qrCode
    }

    fun startScanning() {
        _isScanning.value = true // ✅ Ensures scanner opens
    }

    fun stopScanning() {
        _isScanning.value = false // ✅ Ensures scanner closes
    }
}




class CheckInViewModel(private val apiService: RSVPApiService) : ViewModel() {
    // ✅ Use StateFlow instead of mutableStateOf
    private val _rsvpList = MutableStateFlow<List<RSVPItem>>(emptyList())
    val rsvpList: StateFlow<List<RSVPItem>> = _rsvpList.asStateFlow()

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchApprovedRSVPs()
    }

    // ✅ Fetch Approved RSVPs and update the list reactively
    fun fetchApprovedRSVPs() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                Log.d("CheckInViewModel", "Fetching approved RSVPs...") // ✅ Debug Log
                val response = apiService.getApprovedRSVPs()
                Log.d("CheckInViewModel", "API Response: $response") // ✅ Debug API Response

                if (response.success) {
                    Log.d("CheckInViewModel", "Updated RSVP List: ${response.rsvps}") // ✅ Log Updated List
                    _rsvpList.value = response.rsvps ?: emptyList() // ✅ Update StateFlow
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


    // ✅ Mark User as Checked-In and update UI reactively
    fun markUserCheckedIn(qrCode: String, onCloseScanner: () -> Unit) {
        viewModelScope.launch {
            try {
                val matchingRSVP = _rsvpList.value.find { it.qrcode == qrCode }

                if (matchingRSVP == null) {
                    errorMessage = "QR Code not found in the guest list!"
                    return@launch
                }

                val response = apiService.processQRCode(qrCode, matchingRSVP.partyId.toString())

                if (response.success) {
                    successMessage = "User ${matchingRSVP.name} Checked-In Successfully 🎉"

                    // ✅ Close the scanner
                    onCloseScanner()

                    // ✅ Log before fetching latest list
                    Log.d("CheckInViewModel", "User checked in, refetching RSVPs...")

                    // ✅ Fetch the latest guest list
                    fetchApprovedRSVPs()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            }
        }
    }


    fun clearErrorMessage() {
        errorMessage = null
    }

    fun clearSuccessMessage() {
        successMessage = null
    }
}



