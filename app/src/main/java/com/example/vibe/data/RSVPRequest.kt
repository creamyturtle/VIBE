package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class RSVPRequest(
    val party_id: Int,
    val addguest1: String? = null,
    val addguest2: String? = null,
    val addguest3: String? = null,
    val addguest4: String? = null,
    val bringing: String? = null
)
