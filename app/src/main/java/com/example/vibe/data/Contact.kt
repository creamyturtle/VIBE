package com.example.vibe.data

import retrofit2.http.Body
import retrofit2.http.POST

data class ContactRequest(
    val name: String,
    val email: String,
    val subject: String,
    val message: String
)

data class ContactResponse(
    val success: Boolean,
    val message: String
)

interface ContactApi {
    @POST("contact_api.php") // Adjust URL based on your API location
    suspend fun sendContactMessage(@Body request: ContactRequest): ContactResponse
}
