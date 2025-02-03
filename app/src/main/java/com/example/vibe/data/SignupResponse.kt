package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val status: String,
    val message: String
    //val token: String? = null // ✅ JWT token (nullable for errors)
)