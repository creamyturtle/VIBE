package com.example.vibe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.ContactApi
import com.example.vibe.data.ContactRequest
import com.example.vibe.data.ContactResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel(private val api: ContactApi) : ViewModel() {

    private val _response = MutableStateFlow<ContactResponse?>(null)
    val response: StateFlow<ContactResponse?> = _response

    fun sendContactMessage(name: String, email: String, subject: String, message: String) {
        viewModelScope.launch {
            try {
                val result = api.sendContactMessage(ContactRequest(name, email, subject, message))
                _response.value = result
            } catch (e: Exception) {
                _response.value = ContactResponse(false, "Failed to send message.")
            }
        }
    }
}
