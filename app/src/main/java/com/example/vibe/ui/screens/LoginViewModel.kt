package com.example.vibe.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AuthRepository
import kotlinx.coroutines.launch


class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val success = authRepository.login(email, password)
            if (success) {
                onSuccess()
            } else {
                errorMessage = "Login failed. Check your credentials."
            }
            isLoading = false
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout() // Clear stored token
            Log.d("LoginViewModel", "User logged out. Token should be cleared.")
            onSuccess() // Navigate back to login
        }
    }

}


