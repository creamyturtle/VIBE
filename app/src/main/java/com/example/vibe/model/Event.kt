/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vibe.model

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Data class that defines an amphibian which includes a name, type, description, and image URL.
 */
@Serializable
data class Event(
    val id: String? = null,
    val partyname: String,
    val partytype: String,
    val description: String,
    val openslots: String,
    val totalslots: String,
    val offerings1: String? = "No Special Amenities Offered",
    val offerings2: String? = "No Special Amenities Offered",
    val offerings3: String? = "No Special Amenities Offered",
    val location: String = "Medellin, Colombia",
    val locationlong: String = "Medellin, Colombia",
    val hostgram: String = "@host",
    val date: String,
    val time: String,
    val rules: String? = null,
    val musictype: String? = "No Specific Music Genre",
    val isfreeparking: String? = "0",
    val iswifi: String? = "0",
    val isalcoholprov: String? = "0",
    val ispetfriendly: String? = "0",
    val issmokingallow: String? = "0",
    val videourl: String? = null,
    @SerialName("imageurl1") val imgSrc: String? = null,
    @SerialName("imageurl2") val imgSrc2: String? = null,
    @SerialName("imageurl3") val imgSrc3: String? = null,
    @SerialName("imageurl4") val imgSrc4: String? = null

) {

    val properCaseMusic: String
        get() {
            val safeRules = musictype?.takeIf { it.isNotBlank() } ?: "No Special Amenities Offered"
            return safeRules.split(" ").joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
        }

    val properCaseRules: String
        get() {
            val safeRules = rules?.takeIf { it.isNotBlank() } ?: "No Special Amenities Offered"
            return safeRules.split(" ").joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
        }

    val properCaseOffer1: String
        get() {
            val safeRules = offerings1?.takeIf { it.isNotBlank() } ?: "No Special Amenities Offered"
            return safeRules.split(" ").joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
        }

    val properCaseOffer2: String
        get() {
            val safeRules = offerings2?.takeIf { it.isNotBlank() } ?: "No Special Amenities Offered"
            return safeRules.split(" ").joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
        }

    val properCaseOffer3: String
        get() {
            val safeRules = offerings3?.takeIf { it.isNotBlank() } ?: "No Extra Rule Specified by Host"
            return safeRules.split(" ").joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
        }

    // Computed property for the full image URL
    val fullImgSrc: String
        get() = "https://www.vibesocial.org/$imgSrc"
    val fullImgSrc2: String
        get() = "https://www.vibesocial.org/$imgSrc2"
    val fullImgSrc3: String
        get() = "https://www.vibesocial.org/$imgSrc3"
    val fullImgSrc4: String
        get() = "https://www.vibesocial.org/$imgSrc4"

    val partyMod:String
        get() = when (partytype) {
            "House", "Pool", "Finca" -> "$partytype Party"
            else -> partytype
        }

    val formattedDate: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val parsedDate = LocalDate.parse(date) // Parse the SQL date
            val dayOfWeek = parsedDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            val month = parsedDate.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            val dayOfMonth = parsedDate.dayOfMonth
            val suffix = when {
                dayOfMonth in 11..13 -> "th" // Special case for 11th, 12th, 13th
                dayOfMonth % 10 == 1 -> "st"
                dayOfMonth % 10 == 2 -> "nd"
                dayOfMonth % 10 == 3 -> "rd"
                else -> "th"
            }
            return "$dayOfWeek, $month $dayOfMonth$suffix"
        }

    val formattedTime: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val parsedTime = LocalTime.parse(time) // Parse the SQL time
            return parsedTime.format(DateTimeFormatter.ofPattern("h a")) // Format as "12 PM"
                .lowercase() // Convert to "12pm"
        }


}
