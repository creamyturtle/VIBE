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

import com.example.vibe.network.EventsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val eventsRepository: EventsRepository
}


private val json = Json {
    ignoreUnknownKeys = true // Ignore unknown fields in the JSON response
    isLenient = true // Allow relaxed JSON parsing
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://www.vibesocial.org/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */


    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client) // Add the logging interceptor
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: EventsApiService by lazy {
        retrofit.create(EventsApiService::class.java)
    }

    /**
     * DI implementation for Events repository
     */
    override val eventsRepository: EventsRepository by lazy {
        DefaultEventsRepository(retrofitService)
    }
}
