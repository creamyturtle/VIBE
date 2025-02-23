package com.example.vibe.network

import android.util.Log
import com.example.vibe.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()

        Log.d("AuthInterceptor", "🔍 Retrieved Token: $token") // Log the token

        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "✅ Adding Authorization header: Bearer $token")
            request.addHeader("Authorization", "Bearer $token")
        } else {
            Log.e("AuthInterceptor", "❌ Token is missing! Authentication will fail.")
        }

        return chain.proceed(request.build())
    }
}
