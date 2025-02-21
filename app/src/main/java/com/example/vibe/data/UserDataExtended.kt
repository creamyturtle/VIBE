package com.example.vibe.data

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class UserResponse(
    val status: String,
    val user: MoreUserData?
)

@Serializable
data class MoreUserData(
    val id: Int,
    val name: String,
    val email: String,
    val age: Int,
    val gender: String,
    val facebook: String?,
    val whatsapp: String?,
    val instagram: String,
    val bio: String?,
    val photourl: String?,
    val created_at: String,
    val is_verified: Boolean
) {

    val formattedCreatedAt: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return try {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val outputFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

                val dateTime = LocalDateTime.parse(created_at, inputFormatter)
                dateTime.format(outputFormatter)
            } catch (e: Exception) {
                "Unknown" // Fallback in case of parsing errors
            }
        }


}
