package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class CancelReservationRequest(
    val table_name: String
)

@Serializable
data class CancelResResponse(
    val success: Boolean,
    val message: String?
)