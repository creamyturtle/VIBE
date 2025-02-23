package com.example.vibe.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

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
    val openslots: Int,
    val rsvpapproved: Int,
    val qrcode: String?,
    val tablename: String
)


