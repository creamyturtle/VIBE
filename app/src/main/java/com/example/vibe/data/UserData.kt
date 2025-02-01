package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: Int,
    val name: String,
    val email: String
)