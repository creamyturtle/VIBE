package com.example.vibe.network

import android.util.Log
import com.example.vibe.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()
        val request = chain.request().newBuilder()

        Log.d("AuthInterceptor", "Retrieved Token: $token")

        if (!token.isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $token") // ✅ Attach JWT token
        }

        // ✅ Log the headers before sending the request
        Log.d("AuthInterceptor", "Headers: ${request.build().headers}")

        return chain.proceed(request.build())
    }
}