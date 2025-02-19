package com.example.vibe.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vibe.ui.viewmodel.LanguageViewModel
import com.example.vibe.utils.SessionManager

class LanguageViewModelFactory(
    private val context: Context,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LanguageViewModel(context, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
