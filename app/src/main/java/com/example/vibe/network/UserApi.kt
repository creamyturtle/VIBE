package com.example.vibe.network

import com.example.vibe.data.UserProfileUpdateRequest
import com.example.vibe.data.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {
    @GET("get_user.php")
    suspend fun getUserInfo(): Response<UserResponse>

    @PUT("update_profileApp.php")
    suspend fun updateUserProfile(@Body request: UserProfileUpdateRequest): Response<Unit>

    @POST("remove_photo.php")
    suspend fun removeProfilePhoto(): Response<Unit>
}

