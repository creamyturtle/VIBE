package com.example.vibe.data



data class UploadResponse(
    val success: Boolean,
    val message: String,
    val fileUrl: String?
)