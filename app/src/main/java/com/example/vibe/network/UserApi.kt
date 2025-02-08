package com.example.vibe.network

import com.example.vibe.data.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("get_user.php")
    suspend fun getUserInfo(): Response<UserResponse> // ✅ No need to manually pass token
}

