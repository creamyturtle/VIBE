package com.example.vibe.data

import android.content.Context
import com.example.vibe.network.UserApi

class UserRepository(private val context: Context, private val userApi: UserApi) {

    fun getUserFlow() = UserPreferences.getUserFlow(context)

    suspend fun saveUser(user: MoreUserData?) {
        if (user != null) {
            UserPreferences.saveUser(context, user)
        }
    }

    suspend fun fetchUserData(): MoreUserData? {
        val response = userApi.getUserInfo()
        return if (response.isSuccessful && response.body()?.status == "success") {
            response.body()?.user
        } else {
            null
        }
    }
}
