package com.example.vibe.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UploadResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("fileUrl") val fileUrl: String? = null,
    @SerialName("message") val message: String? = null
)