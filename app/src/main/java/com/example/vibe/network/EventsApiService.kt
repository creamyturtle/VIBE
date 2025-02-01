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

package com.example.vibe.network

import com.example.vibe.data.LoginRequest
import com.example.vibe.data.LoginResponse
import com.example.vibe.model.Event
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query



interface AuthApi {
    @POST("loginApp.php") // ✅ Use the new API file
    suspend fun login(@Body request: LoginRequest): LoginResponse
}



interface EventsApiService {

    // Fetch all rows
    @GET("api/{table}/get")
    suspend fun getAll(
        @Path("table") table: String,
        @Query("api_token") apiToken: String
    ): List<Event>


    // Fetch all rows
    @GET("api/{table}/getHouses")
    suspend fun getHouses(
        @Path("table") table: String,
        @Query("api_token") apiToken: String
    ): List<Event>

    // Fetch all rows
    @GET("api/{table}/getPools")
    suspend fun getPools(
        @Path("table") table: String,
        @Query("api_token") apiToken: String
    ): List<Event>

    // Fetch all rows
    @GET("api/{table}/getFincas")
    suspend fun getFincas(
        @Path("table") table: String,
        @Query("api_token") apiToken: String
    ): List<Event>

    // Fetch all rows
    @GET("api/{table}/getActivities")
    suspend fun getActivities(
        @Path("table") table: String,
        @Query("api_token") apiToken: String
    ): List<Event>




    // Insert a new row
    @POST("api/{table}/insert")
    fun insert(
        @Path("table") table: String,
        @Query("api_token") apiToken: String,
        @Body data: Map<String, Any>
    ): Call<Map<String, Any>>

    // Update a row by ID
    @PUT("api/{table}/update")
    fun update(
        @Path("table") table: String,
        @Query("api_token") apiToken: String,
        @Query("id") id: Int,
        @Body data: Map<String, Any>
    ): Call<Map<String, Any>>

    // Delete a row by ID
    @DELETE("api/{table}/delete")
    fun delete(
        @Path("table") table: String,
        @Query("api_token") apiToken: String,
        @Query("id") id: Int
    ): Call<Map<String, Any>>
}



