package com.example.vibe.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.vibe.utils.SessionManager
import com.example.vibe.utils.setAppLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale


class LanguageViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _language = MutableStateFlow(sessionManager.getLanguage().uppercase(Locale.ROOT))
    val language: StateFlow<String> = _language.asStateFlow()

    fun setLanguage(activity: Activity, navController: NavController, langCode: String) {
        val normalizedLang = langCode.uppercase(Locale.ROOT)

        // ✅ Prevent redundant calls
        if (_language.value == normalizedLang) return

        _language.value = normalizedLang
        sessionManager.saveLanguage(normalizedLang)

        // ✅ Now correctly restarts with navController
        setAppLocale(activity, normalizedLang, navController, shouldRestart = true)
    }
}








