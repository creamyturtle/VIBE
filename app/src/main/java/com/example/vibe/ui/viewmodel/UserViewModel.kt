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
import com.example.vibe.data.UserProfileUpdateRequest
import com.example.vibe.network.UserApi

class UserViewModel(
    private val userRepository: UserRepository,
    private val userApi: UserApi,
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

                // ✅ If there's no cached data, fetch from the API
                if (user == null) {
                    fetchUserData()
                }
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


    fun updateUserProfile(
        name: String, age: Int, gender: String, instagram: String,
        bio: String, facebook: String, whatsapp: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = userApi.updateUserProfile(
                    UserProfileUpdateRequest(
                        name = name,
                        age = age.toString(),
                        gender = if (gender == "Male") 1 else 0,
                        instagram = instagram,
                        bio = bio,
                        facebook = facebook,
                        whatsapp = whatsapp
                    )
                )
                if (response.isSuccessful) {
                    onResult(true, null)
                    fetchUserData() // ✅ Refresh user data after update
                } else {
                    onResult(false, "Update failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun removeProfilePhoto() {
        viewModelScope.launch {
            try {
                val response = userApi.removeProfilePhoto()
                if (response.isSuccessful) {
                    _userData.postValue(_userData.value?.copy(photourl = "https://www.vibesocial.org/images/team5.jpg"))
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to remove profile photo: ${e.message}")
            }
        }
    }



}

