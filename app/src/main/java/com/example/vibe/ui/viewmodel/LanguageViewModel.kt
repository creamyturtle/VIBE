package com.example.vibe.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.vibe.data.TranslationApi
import com.example.vibe.model.Event
import com.example.vibe.utils.SessionManager
import com.example.vibe.utils.setAppLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Locale


class LanguageViewModel(private val translationApi: TranslationApi, private val sessionManager: SessionManager) : ViewModel() {

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


    //auto translator via cached db or google translate

    suspend fun fetchTranslation(text: String): String {
        val targetLang = _language.value.lowercase(Locale.ROOT) // Use selected language
        return withContext(Dispatchers.IO) {
            try {
                val response = translationApi.getTranslation(text, targetLang)
                if (response.isSuccessful) {
                    response.body()?.translated_text ?: text // Return translation or fallback
                } else {
                    Log.e("Translation", "API Error: ${response.errorBody()?.string()}")
                    text // Fallback
                }
            } catch (e: Exception) {
                Log.e("Translation", "Exception: ${e.message}")
                text // Fallback
            }
        }
    }

    suspend fun getTranslatedEvent(event: Event): Event {
        val translatedDescription = fetchTranslation(event.description)
        val translatedOfferings1 = fetchTranslation(event.properCaseOffer1)
        val translatedOfferings2 = fetchTranslation(event.properCaseOffer2)
        val translatedOfferings3 = fetchTranslation(event.properCaseOffer3)
        val translatedRules = fetchTranslation(event.properCaseRules)

        return event.copy(
            description = translatedDescription,
            offerings1 = translatedOfferings1,
            offerings2 = translatedOfferings2,
            offerings3 = translatedOfferings3,
            rules = translatedRules
        )
    }





}








