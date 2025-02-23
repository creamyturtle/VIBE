package com.example.vibe.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.UserPreferences
import com.example.vibe.data.UserPreferences.getDarkModeFlow
import com.example.vibe.data.UserPreferences.saveDarkMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    val isDarkMode = getDarkModeFlow(context).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            saveDarkMode(context, enabled)
        }
    }
}

