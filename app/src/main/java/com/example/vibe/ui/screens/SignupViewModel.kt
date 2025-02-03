package com.example.vibe.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.SignupRequest
import com.example.vibe.network.SignupApi
import kotlinx.coroutines.launch


class SignupViewModel(private val signupApi: SignupApi) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("Male")
    var instagram by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun signup(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val ageInt = age.toIntOrNull()
            if (ageInt == null || ageInt < 18 || ageInt > 120) {
                errorMessage = "Invalid age."
                isLoading = false
                return@launch
            }

            if (password.length < 8) {
                errorMessage = "Password must be at least 8 characters long."
                isLoading = false
                return@launch
            }

            val request = SignupRequest(
                name = name,
                email = email,
                password = password,
                age = ageInt,
                sexismale = gender == "Male",
                instagram = instagram
            )

            try {
                val response = signupApi.signup(request) // ✅ Call API directly
                isLoading = false

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        onSuccess()
                    } else {
                        errorMessage = body?.message ?: "Signup failed. Try again."
                    }
                } else {
                    errorMessage = "Signup failed. Server error: ${response.code()}"
                }

            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Network error: ${e.message}"
            }
        }
    }

}
