package com.example.vibe.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.google.android.play.core.integrity.StandardIntegrityManager
import java.util.Locale

fun setAppLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)


}








