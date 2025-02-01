package com.example.vibe.data

import com.example.vibe.network.AuthApi
import com.example.vibe.utils.SessionManager
import android.util.Log

class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.status == "success" && response.token != null) {
                sessionManager.saveToken(response.token) // ✅ Store JWT token
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Network error during login", e)
            false
        }
    }

    // ✅ Logout function clears the stored token
    fun logout() {
        sessionManager.clearToken()
    }
}

