package com.example.vibe.data

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val status: String,
    val user: MoreUserData?
)

@Serializable
data class MoreUserData(
    val name: String,
    val age: Int,
    val gender: String,
    val instagram: String
)
