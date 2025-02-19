package com.example.vibe.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.vibe.utils.SessionManager
import com.example.vibe.utils.setAppLocale
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import kotlinx.coroutines.launch


class LanguageViewModel(private val context: Context, private val sessionManager: SessionManager) : ViewModel() {

    private val _language = mutableStateOf(sessionManager.getLanguage() ?: "EN")
    val language: State<String> = _language // ✅ This needs the correct import

    fun setLanguage(context: Context, langCode: String) {
        _language.value = langCode
        sessionManager.saveLanguage(langCode)

        viewModelScope.launch {
            setAppLocale(context, langCode)
        }
    }
}





