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

    // ✅ Use application directly (no memory leak)
    private val userDarkModePreference = UserPreferences.getDarkModeFlow(application)

    fun getDarkModeState(systemDarkTheme: Boolean): StateFlow<Boolean> {
        return userDarkModePreference
            .map { userPref -> userPref ?: systemDarkTheme }
            .stateIn(viewModelScope, SharingStarted.Eagerly, systemDarkTheme)
    }

    fun toggleDarkMode(enabled: Boolean?) {
        viewModelScope.launch {
            UserPreferences.saveDarkMode(getApplication(), enabled) // ✅ No context leak
        }
    }
}




