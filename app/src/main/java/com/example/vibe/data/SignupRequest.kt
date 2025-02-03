package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val sexismale: Boolean,
    val instagram: String
)