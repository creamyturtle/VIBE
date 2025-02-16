package com.example.vibe.data

import kotlinx.serialization.Serializable

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
)
