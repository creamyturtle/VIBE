/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vibe.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vibe.VibeApplication
import com.example.vibe.data.EventsRepository
import com.example.vibe.model.Event
import com.example.vibe.utils.uriToMultipartBody
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.File
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface EventsUiState {
    data class Success(val events: List<Event>) : EventsUiState
    object Error : EventsUiState
    object Loading : EventsUiState
}

/**
 * ViewModel containing the app data and method to retrieve the data
 */
class EventsViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    val isUploading = MutableStateFlow(false)

    var eventsUiState: EventsUiState by mutableStateOf(EventsUiState.Loading)
        private set

    var selectedEvent: Event? by mutableStateOf(null)
        private set

    init {
        if (eventsUiState !is EventsUiState.Success) {
            getEvents() // ✅ Ensure events load on ViewModel creation
        }
    }

    fun getEvents() {
        viewModelScope.launch {
            eventsUiState = EventsUiState.Loading
            eventsUiState = try {
                EventsUiState.Success(eventsRepository.getEvents())
            } catch (e: IOException) {
                EventsUiState.Error
            } catch (e: HttpException) {
                EventsUiState.Error
            }
        }
    }

    private var lastFilter = ""

    fun getEventsByType(type: String) {

        if (lastFilter == type) return // ✅ Prevent duplicate API calls
        lastFilter = type

        viewModelScope.launch {
            eventsUiState = EventsUiState.Loading
            eventsUiState = try {
                EventsUiState.Success(eventsRepository.getEventsByType(type))
            } catch (e: Exception) {
                EventsUiState.Error
            }
        }
    }

    fun selectEvent(eventId: String) {
        val events = (eventsUiState as? EventsUiState.Success)?.events
        selectedEvent = events?.find { it.id == eventId }
    }

    /**
     * Factory for [EventsViewModel] that takes [EventsRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as VibeApplication)
                val eventsRepository = application.container.eventsRepository
                EventsViewModel(eventsRepository = eventsRepository)
            }
        }
    }


    fun submitEventWithMedia(
        context: Context,
        event: Event,
        selectedImages: List<Uri>,
        selectedVideo: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onProgressUpdate: (String) -> Unit // <-- ✅ Added callback for UI updates
    ) {
        viewModelScope.launch {
            try {
                isUploading.value = true

                onProgressUpdate("Uploading media...") // ✅ Show progress start

                val uploadedImageUrls = mutableListOf<String>()

                Log.d("SubmitEvent", "Starting image upload. Selected images: ${selectedImages.size}")


                for (uri in selectedImages) {
                    onProgressUpdate("Uploading image...")
                    val imagePart = uriToMultipartBody(context, uri, "image") ?: continue
                    val response = eventsRepository.uploadMedia(imagePart)
                    if (response.success && response.fileUrl != null) {
                        uploadedImageUrls.add(response.fileUrl)
                        onProgressUpdate("Image uploaded successfully!")
                    } else {
                        Log.e("Upload", "Upload failed or fileUrl is null")
                        onProgressUpdate("Failed to upload an image.")
                    }
                }

                // ✅ Log before video upload
                if (selectedVideo != null) {
                    Log.d("SubmitEvent", "Starting video upload: ${selectedVideo.lastPathSegment}")
                } else {
                    Log.d("SubmitEvent", "No video selected.")
                }

                val uploadedVideoUrl = selectedVideo?.let { selectedUri ->
                    onProgressUpdate("Uploading video...")
                    try {
                        withTimeout(60_000) { // ⏳ Set a timeout of 30 seconds
                            val videoPart = uriToMultipartBody(context, selectedUri, "video")

                            if (videoPart == null) {
                                Log.e("UploadDebug", "Error: Failed to create MultipartBody for video.")
                                onProgressUpdate("Video upload failed! Please try again.")
                                return@withTimeout null
                            }

                            val videoFile = File(context.cacheDir, "upload_video.mp4")
                            Log.d("UploadDebug", "File exists: ${videoFile.exists()}, Size: ${videoFile.length()} bytes")

                            Log.d("UploadDebug", "Uploading file: ${selectedVideo?.path}")
                            Log.d("UploadDebug", "File size: ${selectedVideo?.let { File(it.path!!).length() } ?: "N/A"} bytes")
                            Log.d("UploadDebug", "Request Headers: ${videoPart.headers}")
                            Log.d("UploadDebug", "Request Content Type: ${videoPart.body.contentType()}")



                            val response = eventsRepository.uploadMedia(videoPart)

                            if (response.success) {
                                Log.d("UploadDebug", "Video upload complete. URL: ${response.fileUrl}")
                                onProgressUpdate("Video uploaded successfully!")
                                response.fileUrl
                            } else {
                                Log.e("UploadDebug", "Video upload failed!")
                                onProgressUpdate("Video upload failed! Please try again.")
                                null
                            }
                        }
                    } catch (e: TimeoutCancellationException) {
                        Log.e("UploadDebug", "Upload timeout! Video upload took too long.")
                        onProgressUpdate("Upload timeout! Try again.")
                        null
                    }
                }


                val updatedEvent = event.copy(
                    imgSrc = uploadedImageUrls.getOrNull(0),
                    imgSrc2 = uploadedImageUrls.getOrNull(1),
                    imgSrc3 = uploadedImageUrls.getOrNull(2),
                    imgSrc4 = uploadedImageUrls.getOrNull(3),
                    videourl = uploadedVideoUrl ?: event.videourl // Keep typed URL if no upload
                )

                // ✅ Log final JSON before submission
                Log.d("SubmitEvent", "Final Event JSON: ${Json.encodeToString(updatedEvent)}")

                onProgressUpdate("Submitting event...")

                val response = eventsRepository.submitEvent(updatedEvent)
                if (response.success) {
                    onProgressUpdate("Event submitted successfully!")
                    onSuccess()
                    Log.d("SubmitEvent", "Event submission successful!")
                } else {
                    onProgressUpdate("Error: ${response.message}")
                    onError(response.message)
                    Log.e("SubmitEvent", "Event submission failed: ${response.message}")
                }
            } catch (e: Exception) {
                onProgressUpdate("Error: ${e.message}")
                onError(e.message ?: "Unknown error")
            } finally {
                isUploading.value = false // ✅ Ensure spinner stops even if there's an error
            }
        }
    }




}
