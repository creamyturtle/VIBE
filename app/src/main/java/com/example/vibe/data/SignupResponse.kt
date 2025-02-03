package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val success: Boolean,
    val message: String,
    val emailSent: Boolean? = null
)