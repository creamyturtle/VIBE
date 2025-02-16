package com.example.vibe.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun uriToMultipartBody(context: Context, uri: Uri, paramName: String): MultipartBody.Part? {
    return try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null

        // ✅ Create a temp file in cache directory
        val tempFile = File(context.cacheDir, "upload_video.mp4").apply {
            outputStream().use { inputStream.copyTo(it) }
        }

        // ✅ Check file size after writing
        val fileSize = tempFile.length()
        Log.d("UploadDebug", "Copied file to cache: ${tempFile.absolutePath}, Size: $fileSize bytes")

        // 🚨 **CRITICAL FIX:** Check if file is non-zero before proceeding
        if (fileSize == 0L) {
            Log.e("UploadDebug", "Error: File size is 0 bytes after copying.")
            return null
        }

        // ✅ Ensure proper `RequestBody` setup
        val requestFile = tempFile.asRequestBody("video/mp4".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(paramName, tempFile.name, requestFile)

        Log.d("UploadDebug", "MultipartBody Created: ${multipartBody.body.contentLength()} bytes")
        multipartBody

    } catch (e: Exception) {
        Log.e("UploadDebug", "Error converting URI to MultipartBody: ${e.message}", e)
        null
    }
}


