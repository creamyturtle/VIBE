package com.example.vibe.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RSVPApiService {

    @GET("approveReservationsApp.php?action=fetch")
    suspend fun getPendingRSVPs(
        @Query("filter") filter: String = "pending",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): RSVPResponse

    @GET("approveReservationsApp.php?action=approve")
    suspend fun approveRSVP(
        @Query("approve_id") approveId: Int,
        @Query("party_id") partyId: Int
    ): ApprovalResponse







}

// Data Models

@Serializable
data class ApprovalResponse(val success: Boolean, val message: String)

@Serializable
data class RSVPResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("rsvps") val rsvps: List<RSVPItem>? = emptyList() // ✅ Allow `rsvps` to be null
)

@Serializable
data class RSVPItem(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("malefemale") val gender: String,
    @SerialName("age") val age: Int,
    @SerialName("instagram") val instagram: String?,
    @SerialName("addguest1") val addguest1: String?,
    @SerialName("addguest2") val addguest2: String?,
    @SerialName("addguest3") val addguest3: String?,
    @SerialName("addguest4") val addguest4: String?,
    @SerialName("bringing") val bringing: String?,
    @SerialName("rsvpapproved") val rsvpApproved: Int,
    @SerialName("partyname") val partyName: String,
    @SerialName("party_id") val partyId: Int
)
