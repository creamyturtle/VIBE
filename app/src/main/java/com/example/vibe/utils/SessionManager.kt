package com.example.vibe.utils


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.navigation.NavController
import com.example.vibe.data.UserData
import org.json.JSONObject
import java.util.Locale

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_LANGUAGE = "selected_language"
    }

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun getUserData(): UserData? {
        val token = getToken() ?: return null

        return try {
            val payload = token.split(".")[1] // JWT format: header.payload.signature
            val decodedPayload = decodeBase64(payload)
            val json = JSONObject(decodedPayload)

            UserData(
                id = json.optInt("user_id", -1), // Default to -1 if missing
                name = json.optString("name", "Unknown"),
                email = json.optString("email", "Unknown")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null on failure
        }
    }



    /**
     * ✅ Helper function to decode JWT Base64 safely
     * - Handles missing padding
     * - Uses correct Base64 mode
     */

    fun decodeBase64(base64String: String): String {
        var modifiedString = base64String.replace("-", "+").replace("_", "/") // Fix URL-safe encoding
        while (modifiedString.length % 4 != 0) {
            modifiedString += "=" // Ensure proper padding
        }
        return String(Base64.decode(modifiedString, Base64.DEFAULT), Charsets.UTF_8)
    }



    fun isTokenExpired(): Boolean {
        val token = getToken() ?: return true
        return try {
            val payload = token.split(".")[1]
            val decodedPayload = decodeBase64(payload)
            val json = JSONObject(decodedPayload)

            val exp = json.optLong("exp", 0) // Expiration time in seconds
            val currentTime = System.currentTimeMillis() / 1000 // Convert to seconds

            exp < currentTime // ✅ True if expired
        } catch (e: Exception) {
            true // Assume expired if there's an error
        }
    }



    fun clearToken() {
        prefs.edit().remove("auth_token").apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    // ✅ Save selected language
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: Locale.getDefault().language
    }

    // Save the selected language
    fun saveLanguage(langCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply()
    }

    // Apply language at app startup
    fun applySavedLanguage(activity: Activity, navController: NavController?) {
        val language = getLanguage()
        navController?.let {
            setAppLocale(activity, language, it, shouldRestart = false) // ✅ Ensure correct locale on startup
        }
    }



}
