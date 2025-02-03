package com.example.vibe.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.vibe.data.SignupRequest
import com.example.vibe.data.SignupResponse
import com.example.vibe.network.SignupApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val signupApi: SignupApi) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("Male") // Default to Male
    var instagram by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    suspend fun signup(onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || age.isBlank() || instagram.isBlank()) {
            errorMessage = "All fields are required."
            return
        }

        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt < 18 || ageInt > 120) {
            errorMessage = "Invalid age."
            return
        }

        if (password.length < 8) {
            errorMessage = "Password must be at least 8 characters long."
            return
        }

        isLoading = true
        errorMessage = null

        val request = SignupRequest(
            name = name,
            email = email,
            password = password,
            age = ageInt,
            sexismale = gender == "Male", // Convert gender to Boolean
            instagram = instagram
        )

        signupApi.signup(request).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                isLoading = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            onSuccess()
                        } else {
                            errorMessage = it.message
                        }
                    }
                } else {
                    errorMessage = "Signup failed. Try again."
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                isLoading = false
                errorMessage = "Network error: ${t.message}"
            }
        })
    }
}