package com.example.vibe.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.vibe.utils.SessionManager
import com.example.vibe.utils.setAppLocale
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale


class LanguageViewModel(private val context: Context, private val sessionManager: SessionManager) : ViewModel() {

    private val _language = MutableStateFlow(sessionManager.getLanguage()?.uppercase(Locale.ROOT) ?: "EN")
    val language: StateFlow<String> = _language.asStateFlow() // ✅ Use StateFlow

    fun setLanguage(context: Context, langCode: String) {
        val normalizedLang = langCode.uppercase(Locale.ROOT)
        _language.value = normalizedLang
        sessionManager.saveLanguage(normalizedLang)

        viewModelScope.launch {
            setAppLocale(context, normalizedLang)
        }
    }
}






