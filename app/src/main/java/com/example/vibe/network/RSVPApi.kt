package com.example.vibe.network

import com.example.vibe.data.RSVPRequest
import com.example.vibe.data.RSVPResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Path

interface RSVPApi {

    @POST("rsvp_api.php")
    @Headers(
        "Content-Type: application/json",
        "User-Agent: VibeApp Android Client" // ✅ This makes the request look more like a normal browser
    )
    suspend fun submitRSVP(
        @Body rsvpRequest: RSVPRequest
    ): Response<RSVPResponse>


    @GET("rsvp/status/{partyId}")
    suspend fun getRSVPStatus(@Path("partyId") partyId: Int): RSVPResponse



    @GET("rsvp_api.php")
    suspend fun checkRSVP(
        @Header("Authorization") token: String,
        @Query("party_id") partyId: Int
    ): Response<RSVPResponse>



}
