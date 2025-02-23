package com.example.vibe.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class AttendingResponse(
    @SerializedName("events") val events: List<EventAttending>
)

@Serializable
data class EventAttending(
    val id: Int,
    val partyname: String,
    val date: String,
    val time: String,
    val location: String,
    val locationlong: String,
    val openslots: Int,
    val totalslots: Int,
    val rsvpapproved: Int,
    val qrcode: String,
    val tablename: String,

    @SerialName("imageurl1") val imgSrc: String? = null
) {

    val fullImgSrc: String
        get() = "https://www.vibesocial.org/$imgSrc"

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


