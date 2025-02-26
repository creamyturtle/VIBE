package com.example.vibe.data

import retrofit2.http.*
import kotlinx.serialization.Serializable

interface QRCodeApi {
    @FormUrlEncoded
    @POST("process_qr_App.php")
    suspend fun processQRCode(
        @Field("qrcode") qrcode: String,
        @Field("table") table: String
    ): QRResponse
}



@Serializable
data class QRResponse(
    val status: String,
    val message: String
)
