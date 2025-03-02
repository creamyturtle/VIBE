package com.example.vibe.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.utils.SessionManager
import com.example.vibe.utils.setAppLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale


class LanguageViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _language = MutableStateFlow(sessionManager.getLanguage().uppercase(Locale.ROOT))
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






