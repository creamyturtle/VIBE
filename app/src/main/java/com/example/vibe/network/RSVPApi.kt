package com.example.vibe.network

import com.example.vibe.data.RSVPRequest
import com.example.vibe.data.RSVPResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RSVPApi {

    @POST("rsvp_api.php")
    @Headers(
        "Content-Type: application/json",
        "User-Agent: VibeApp Android Client" // ✅ This makes the request look more like a normal browser
    )
    suspend fun submitRSVP(
        @Body rsvpRequest: RSVPRequest
    ): Response<RSVPResponse>


}
