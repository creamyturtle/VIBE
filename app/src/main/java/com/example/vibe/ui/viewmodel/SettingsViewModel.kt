package com.example.vibe.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val userDarkModePreference = UserPreferences.getDarkModeFlow(application)

    private val _darkModeState = MutableStateFlow<Boolean?>(null) // ✅ Start as null
    val darkModeState: StateFlow<Boolean?> = _darkModeState.asStateFlow()

    fun resolveDarkMode(systemDarkTheme: Boolean) {
        viewModelScope.launch {
            userDarkModePreference.collect { userPref ->
                val resolvedTheme = userPref ?: systemDarkTheme // ✅ Apply system theme only if no user preference
                if (_darkModeState.value != resolvedTheme) { // ✅ Prevent duplicate emissions
                    _darkModeState.value = resolvedTheme
                }
            }
        }
    }

    fun toggleDarkMode(enabled: Boolean?) {
        viewModelScope.launch {
            UserPreferences.saveDarkMode(getApplication(), enabled)
        }
    }
}






