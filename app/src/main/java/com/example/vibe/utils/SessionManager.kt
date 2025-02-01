package com.example.vibe.utils


import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.vibe.data.UserData
import org.json.JSONObject

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun getUserData(): UserData? {
        val token = getToken() ?: return null
        val payload = token.split(".")[1] // JWT format: header.payload.signature
        val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE), Charsets.UTF_8)
        val json = JSONObject(decodedPayload)

        return UserData(
            id = json.getInt("user_id"),
            name = json.getString("name"),
            email = json.getString("email")
        )
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

}
