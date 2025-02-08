package com.example.vibe.data

import kotlinx.serialization.Serializable


@Serializable
data class RSVPResponse(
    val status: String? = null,
    val message: String? = null,
    val user: RSVPUserData? = null,
    val party: PartyData? = null,
    val error: String? = null
)


@Serializable
data class RSVPUserData(
    val name: String,
    val age: Int,
    val gender: String,
    val instagram: String
)



@Serializable
data class PartyData(
    val id: Int,
    val name: String,
    val host_id: Int
)

