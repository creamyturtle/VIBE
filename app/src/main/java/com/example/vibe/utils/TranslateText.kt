package com.example.vibe.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.vibe.ui.viewmodel.LanguageViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
fun TranslateText(text: String, languageViewModel: LanguageViewModel) {
    val targetLang by languageViewModel.language.collectAsState() // Observe language
    var translatedText by remember { mutableStateOf(text) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(text, targetLang) {
        coroutineScope.launch {
            translatedText = languageViewModel.fetchTranslation(text)
        }
    }

    Text(text = translatedText)
}

