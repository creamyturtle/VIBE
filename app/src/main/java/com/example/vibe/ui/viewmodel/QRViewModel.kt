package com.example.vibe.ui.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AppContainer
import com.example.vibe.network.RSVPApiService
import com.example.vibe.network.RSVPItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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


    fun markUserCheckedIn(qrCode: String, onCloseScanner: () -> Unit) {
        viewModelScope.launch {
            try {
                val matchingRSVP = rsvpList.find { it.qrcode == qrCode }

                if (matchingRSVP == null) {
                    errorMessage = "QR Code not found in the guest list!"
                    return@launch
                }

                val response = apiService.processQRCode(qrCode, matchingRSVP.partyId.toString())

                if (response.success) {
                    successMessage = "User ${matchingRSVP.name} Checked-In Successfully 🎉"

                    // ✅ Close the scanner (so it doesn't keep scanning)
                    onCloseScanner()

                    // ✅ Fetch updated guest list
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


