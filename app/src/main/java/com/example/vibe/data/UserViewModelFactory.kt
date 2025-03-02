package com.example.vibe.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vibe.network.UserApi
import com.example.vibe.ui.viewmodel.UserViewModel
import com.example.vibe.utils.SessionManager

class UserViewModelFactory(
    private val userApi: UserApi,
    private val sessionManager: SessionManager,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST") // ✅ Suppress only for the cast
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            val userRepository = UserRepository(context, userApi) // ✅ Create Repository
            return UserViewModel(userRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}


