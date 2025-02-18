package com.example.vibe.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Locale


fun uriToMultipartBody(context: Context, uri: Uri, paramName: String = "file"): MultipartBody.Part? {
    val contentResolver = context.contentResolver

    // ✅ Get actual file extension
    val fileExtension = getFileExtension(context, uri) ?: "jpg" // Default to "jpg"

    val inputStream = contentResolver.openInputStream(uri) ?: return null

    // ✅ Create a temp file with the correct extension
    val tempFile = File.createTempFile("upload", ".$fileExtension", context.cacheDir).apply {
        outputStream().use { inputStream.copyTo(it) }
    }

    Log.d("UploadDebug", "Temp File Created: ${tempFile.absolutePath}, Size: ${tempFile.length()} bytes")

    // ✅ Use the correct MIME type
    val mimeType = getMimeType(fileExtension)
    val requestFile = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())

    Log.d("UploadDebug", "Uploading file with name: ${tempFile.name}, mimeType: $mimeType")

    // ✅ Explicitly create the multipart part
    return MultipartBody.Part.createFormData(paramName, tempFile.name, requestFile)
}



// ✅ Function to get the correct file extension
private fun getFileExtension(context: Context, uri: Uri): String? {
    return context.contentResolver.getType(uri)?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
        ?: MimeTypeMap.getFileExtensionFromUrl(uri.toString())
}

// ✅ Function to get MIME type based on extension
private fun getMimeType(extension: String): String {
    return when (extension.lowercase(Locale.ROOT)) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "mp4" -> "video/mp4"
        "mov" -> "video/quicktime"
        "avi" -> "video/x-msvideo"
        else -> "application/octet-stream" // Fallback for unknown types
    }
}


