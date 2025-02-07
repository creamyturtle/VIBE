package com.example.vibe.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface RSVPApi {

    @POST("api/rsvp_api.php")
    suspend fun submitRSVP(
        @Header("Authorization") token: String,
        @Body rsvpRequest: RSVPRequest
    ): Response<RSVPResponse>

    @GET("api/rsvp_api.php")
    suspend fun checkRSVP(
        @Header("Authorization") token: String,
        @Query("party_id") partyId: Int
    ): Response<RSVPResponse>
}
