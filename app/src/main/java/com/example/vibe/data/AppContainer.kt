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

import com.example.vibe.network.AuthApi
import com.example.vibe.network.EventsApiService
import com.example.vibe.utils.SessionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import android.content.Context
import com.example.vibe.network.AuthInterceptor
import com.example.vibe.network.RSVPApi
import com.example.vibe.network.SignupApi
import com.example.vibe.network.UserApi


/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val sessionManager: SessionManager
    val eventsRepository: EventsRepository
    val authApi: AuthApi
    val signupApi: SignupApi
    val rsvpApi: RSVPApi
    val userApi: UserApi
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
class DefaultAppContainer(context: Context) : AppContainer {
    private val BASE_URL = "https://www.vibesocial.org/"

    override val sessionManager = SessionManager(context)

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */


    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionManager))
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

    override val rsvpApi: RSVPApi by lazy {
        retrofit.create(RSVPApi::class.java)
    }

    override val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    // ✅ Add AuthApi service
    override val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    // ✅ Signup Api service
    override val signupApi: SignupApi by lazy {
        retrofit.create(SignupApi::class.java)
    }


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
