package com.example.vibe.data

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class ContactRequest(
    val name: String,
    val email: String,
    val subject: String,
    val message: String
)

@Serializable
data class ContactResponse(
    val success: Boolean,
    val message: String
)

interface ContactApi {
    @POST("contact_api.php") // Adjust URL based on your API location
    suspend fun sendContactMessage(@Body request: ContactRequest): ContactResponse
}
