package com.example.vibe.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AuthRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("")
    var instagram by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun signup(onSuccess: () -> Unit) {
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



}