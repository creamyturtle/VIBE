package com.example.vibe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.RSVPRequest
import com.example.vibe.network.RSVPApi
import com.example.vibe.utils.SessionManager
import kotlinx.coroutines.launch

class RSVPViewModel(private val rsvpApi: RSVPApi, private val sessionManager: SessionManager) : ViewModel() {

    private val _rsvpStatus = MutableLiveData<Result<RSVPResponse>>()
    val rsvpStatus: LiveData<Result<RSVPResponse>> = _rsvpStatus

    fun submitRSVP(partyId: Int, guest1: String?, guest2: String?, guest3: String?, guest4: String?, bringing: String?) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${sessionManager.getToken()}" // Get stored JWT token
                val request = RSVPRequest(partyId, guest1, guest2, guest3, guest4, bringing)
                val response = rsvpApi.submitRSVP(token, request)

                if (response.isSuccessful && response.body() != null) {
                    _rsvpStatus.postValue(Result.success(response.body()!!))
                } else {
                    _rsvpStatus.postValue(Result.failure(Exception("API Error: ${response.errorBody()?.string()}")))
                }
            } catch (e: Exception) {
                _rsvpStatus.postValue(Result.failure(e))
            }
        }
    }
}
