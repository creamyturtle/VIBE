package com.example.vibe.data

import kotlinx.serialization.Serializable


@Serializable
data class UserProfileUpdateRequest(
    val name: String,
    val age: String,
    val gender: Int,
    val instagram: String,
    val bio: String,
    val facebook: String,
    val whatsapp: String
)

