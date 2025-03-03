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
                sessionManager.saveToken(response.token) // ✅ Save token securely
                true
            } else {
                false // ✅ Return false instead of crashing
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login failed", e)
            false // ✅ Prevent crashes by safely handling exceptions
        }
    }


}

