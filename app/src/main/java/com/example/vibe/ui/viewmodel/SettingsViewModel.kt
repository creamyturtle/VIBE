package com.example.vibe.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    // ✅ Read user preference (nullable, null means "follow system")
    private val userDarkModePreference = UserPreferences.getDarkModeFlow(context)

    // ✅ Function to update theme based on system setting
    fun getDarkModeState(systemDarkTheme: Boolean): StateFlow<Boolean> {
        return userDarkModePreference
            .map { userPref -> userPref ?: systemDarkTheme } // Use system theme if null
            .stateIn(viewModelScope, SharingStarted.Eagerly, systemDarkTheme)
    }

    /** ✅ Save user-selected dark mode, overriding system setting */
    fun toggleDarkMode(enabled: Boolean?) {
        viewModelScope.launch {
            UserPreferences.saveDarkMode(context, enabled)
        }
    }
}



