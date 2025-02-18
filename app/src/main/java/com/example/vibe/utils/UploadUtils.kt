package com.example.vibe.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun uriToMultipartBody(context: Context, uri: Uri, paramName: String = "file"): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val tempFile = File.createTempFile("upload", ".mp4", context.cacheDir).apply {
        outputStream().use { inputStream.copyTo(it) }
    }

    Log.d("UploadDebug", "Temp File Path: ${tempFile.absolutePath}, Size: ${tempFile.length()} bytes")

    val requestFile = tempFile.asRequestBody("video/mp4".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
}





