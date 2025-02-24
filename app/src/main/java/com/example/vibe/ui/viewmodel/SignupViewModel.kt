package com.example.vibe.ui.viewmodel


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

    fun validateFields(): String? {
        if (name.isBlank()) return "Name is required"
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Enter a valid email address"
        if (password.length < 8) return "Password must be at least 8 characters long"
        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt < 18 || ageInt > 120) return "Enter a valid age (18-120)"
        if (instagram.isBlank()) return "Instagram handle is required"

        return null // All fields are valid
    }

    fun signup(onSuccess: () -> Unit) {
        val validationMessage = validateFields()
        if (validationMessage != null) {
            errorMessage = validationMessage
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val request = SignupRequest(
                name = name,
                email = email,
                password = password,
                age = age.toInt(),
                sexismale = gender == "Male",
                instagram = instagram
            )

            try {
                val response = signupApi.signup(request)
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
