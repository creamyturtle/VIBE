package com.example.vibe.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AuthRepository
import com.example.vibe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _isLoggedIn = MutableStateFlow(sessionManager.isLoggedIn()) // ✅ Uses SessionManager
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun getToken() {
        sessionManager.getToken()
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            errorMessage = null

            try {
                val success = authRepository.login(email, password)
                if (success) {
                    val token = sessionManager.getToken()
                    if (token != null) {
                        _isLoggedIn.value = true
                        withContext(Dispatchers.Main) { // ✅ Switch to main thread for Toast
                            onSuccess()
                        }
                    } else {
                        errorMessage = "Login failed: No token received"
                    }
                } else {
                    errorMessage = "Invalid email or password. Please try again."
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    fun logout(context: Context) {
        sessionManager.clearToken()
        _isLoggedIn.value = sessionManager.isLoggedIn()

        // ✅ Show Toast message on the main thread
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }


}



