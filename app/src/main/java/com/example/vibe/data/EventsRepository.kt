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

package com.example.vibe.data

import android.util.Log
import com.example.vibe.model.Event
import com.example.vibe.network.EventsApiService
import com.example.vibe.utils.SessionManager
import com.google.android.libraries.places.api.model.kotlin.autoCompleteSessionToken
import okhttp3.MultipartBody

/**
 * Repository retrieves event data from underlying data source.
 */
interface EventsRepository {
    /** Retrieves list of events from underlying data source */
    suspend fun getEvents(): List<Event>
    suspend fun getEventsByType(type: String): List<Event>
    suspend fun submitEvent(event: Event): ApiResponse
    suspend fun uploadMedia(file: MultipartBody.Part): UploadResponse
    suspend fun getAttending(): List<EventAttending>
    suspend fun cancelReservation(tableName: String): CancelResResponse
    suspend fun getEventsByID(): List<Event>

}



/**
 * Network Implementation of repository that retrieves event data from underlying data source.
 */
class DefaultEventsRepository(
    private val eventsApiService: EventsApiService
) : EventsRepository {


    /** Retrieves list of events from underlying data source */
    override suspend fun getEvents(): List<Event> {
        return try {
            val data = eventsApiService.getAll(table = "parties", apiToken = secretToken)
            Log.d("Repository", "Fetched data: $data") // Log data
            data
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching data: ${e.message}", e)
            emptyList() // Return an empty list in case of error
        }
    }

    override suspend fun getEventsByType(type: String): List<Event> {
        return try {
            when (type) {
                "House" -> eventsApiService.getHouses(table = "parties", apiToken = secretToken)
                "Pool" -> eventsApiService.getPools(table = "parties", apiToken = secretToken)
                "Finca" -> eventsApiService.getFincas(table = "parties", apiToken = secretToken)
                "Activity" -> eventsApiService.getActivities(table = "parties", apiToken = secretToken)
                else -> getEvents() // Default to all events
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching $type data: ${e.message}", e)
            emptyList()
        }
    }


    override suspend fun getEventsByID(): List<Event> {
        return try {
            val data = eventsApiService.getByID(table = "parties", apiToken = secretToken)
            Log.d("Repository", "Fetched data: $data") // Log data
            data
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching data: ${e.message}", e)
            emptyList() // Return an empty list in case of error
        }
    }


    override suspend fun submitEvent(event: Event): ApiResponse {
        return try {
            eventsApiService.insertEvent(event) // ✅ Now returns ApiResponse directly
        } catch (e: Exception) {
            Log.e("Repository", "Error submitting event: ${e.message}", e)
            ApiResponse(success = false, message = e.message ?: "Unknown error")
        }
    }


    override suspend fun getAttending(): List<EventAttending> {
        return try {
            val response = eventsApiService.getAttending() // ✅ Now returns AttendingResponse
            Log.d("Repository", "Fetched data: $response")
            response.events ?: emptyList() // ✅ Ensure it doesn't return null
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching data: ${e.message}", e)
            emptyList()
        }
    }



    override suspend fun cancelReservation(tableName: String): CancelResResponse {
        return try {
            val response = eventsApiService.cancelReservation(CancelReservationRequest(tableName))
            Log.d("Repository", "Fetched data: $response")
            response
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching data: ${e.message}", e)
            CancelResResponse(success = false, message = e.message ?: "Unknown error")
        }
    }








    override suspend fun uploadMedia(file: MultipartBody.Part): UploadResponse {
        return try {
            Log.d("UploadDebug", "Starting upload for: ${file.body.contentLength()} bytes")

            val response = eventsApiService.uploadMedia(file)

            Log.d("UploadDebug", "Response received: ${response.success}, message: ${response.message}")

            if (response.success) {
                response
            } else {
                UploadResponse(false, "Upload failed: ${response.message}", null)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error uploading media: ${e.message}", e)
            UploadResponse(false, e.message ?: "Unknown error", null)
        }
    }






}
