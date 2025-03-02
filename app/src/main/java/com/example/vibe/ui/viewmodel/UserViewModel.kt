package com.example.vibe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.MoreUserData
import com.example.vibe.data.UserRepository
import com.example.vibe.utils.SessionManager
import kotlinx.coroutines.launch


class UserViewModel(
    private val userRepository: UserRepository, // ✅ Use repository instead of context
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userData = MutableLiveData<MoreUserData?>()
    val userData: LiveData<MoreUserData?> = _userData
    val userId: LiveData<Int?> = userData.map { it?.id }

    init {
        loadCachedUser()
    }

    private fun loadCachedUser() {
        viewModelScope.launch {
            userRepository.getUserFlow().collect { user ->
                _userData.postValue(user)
            }
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            try {
                val user = userRepository.fetchUserData()
                _userData.postValue(user)

                if (user != null) {
                    userRepository.saveUser(user)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "❌ Exception fetching user data: ${e.localizedMessage}")
                _userData.postValue(null)
            }
        }
    }
}

