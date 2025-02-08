package com.example.vibe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.RSVPRequest
import com.example.vibe.data.RSVPResponse
import com.example.vibe.network.RSVPApi
import com.example.vibe.utils.SessionManager
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class RSVPViewModel(private val rsvpApi: RSVPApi, private val sessionManager: SessionManager) : ViewModel() {

    private val _rsvpStatus = MutableLiveData<Result<RSVPResponse>>()
    val rsvpStatus: LiveData<Result<RSVPResponse>> = _rsvpStatus

    fun submitRSVP(partyId: Int, guest1: String?, guest2: String?, guest3: String?, guest4: String?, bringing: String?) {
        viewModelScope.launch {
            try {
                val token = sessionManager.getToken()
                if (token.isNullOrBlank()) {
                    Log.e("RSVPViewModel", "JWT Token is missing or empty")
                    _rsvpStatus.postValue(Result.failure(Exception("Authentication token missing.")))
                    return@launch
                }

                val request = RSVPRequest(
                    party_id = partyId,
                    addguest1 = guest1,
                    addguest2 = guest2,
                    addguest3 = guest3,
                    addguest4 = guest4,
                    bringing = bringing
                )

                // ✅ Log request data before sending
                Log.d("RSVPViewModel", "🔼 Sending RSVP Request: ${Json.encodeToString(request)}")
                Log.d("RSVPViewModel", "🔼 Authorization Header: $token")

                // ✅ Log request data before sending
                val requestJson = Json.encodeToString(RSVPRequest.serializer(), request)
                Log.d("RSVPViewModel", "Sending JSON: $requestJson")

                // ✅ Ensure token format and set Content-Type
                //val authHeader = "Bearer $token"
                val response = rsvpApi.submitRSVP(request)

                if (response.isSuccessful && response.body() != null) {
                    val responseData = response.body()!!
                    Log.d("RSVPViewModel", "RSVP API Response: $responseData") // ✅ Log entire response
                    _rsvpStatus.postValue(Result.success(responseData))
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e("RSVPViewModel", "API Error: $errorResponse") // ✅ Log API failure reason
                    _rsvpStatus.postValue(Result.failure(Exception("API Error: $errorResponse")))
                }
            } catch (e: Exception) {
                Log.e("RSVPViewModel", "Exception: ${e.message}")
                _rsvpStatus.postValue(Result.failure(e))
            }
        }
    }


}

