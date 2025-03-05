package com.example.vibe.utils

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.navigation.NavController
import java.util.Locale

fun setAppLocale(activity: Activity, languageCode: String, navController: NavController, shouldRestart: Boolean = true) {
    val sessionManager = SessionManager(activity)
    sessionManager.saveLanguage(languageCode) // ✅ Save selected language before restart

    val currentLocale = activity.resources.configuration.locales[0].language
    if (currentLocale == languageCode) return // ✅ Prevent redundant restarts

    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration()
    config.setLocale(locale)
    activity.resources.updateConfiguration(config, activity.resources.displayMetrics)

    if (shouldRestart) {
        // ✅ Get current route, but ensure `filterType` is present
        val currentDestination = navController.currentDestination?.route ?: "home_screen/all"

        // ✅ If user is on home screen but `filterType` is missing, default to `all`
        val safeDestination = if (currentDestination.startsWith("home_screen")) {
            "home_screen/all" // ✅ Provide a default filterType
        } else {
            currentDestination
        }

        val intent = Intent(activity, activity::class.java).apply {
            putExtra("NAV_DESTINATION", safeDestination) // ✅ Always provide a valid route
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        activity.startActivity(intent)
    }
}

















