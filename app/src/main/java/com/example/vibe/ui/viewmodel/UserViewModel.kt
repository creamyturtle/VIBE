package com.example.vibe.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.MoreUserData
import com.example.vibe.data.UserPreferences
import com.example.vibe.network.UserApi
import com.example.vibe.utils.SessionManager
import kotlinx.coroutines.launch
import androidx.lifecycle.map


class UserViewModel(
    private val userApi: UserApi,
    private val sessionManager: SessionManager,
    private val context: Context
) : ViewModel() {

    private val _userData = MutableLiveData<MoreUserData?>()
    val userData: LiveData<MoreUserData?> = _userData

    val userId: LiveData<Int?> = userData.map { it?.id }


    init {
        loadCachedUser() // ✅ Load stored user data from DataStore
    }

    /** ✅ Fetch from DataStore first */
    private fun loadCachedUser() {
        viewModelScope.launch {
            UserPreferences.getUserFlow(context).collect { user ->
                _userData.postValue(user)
            }
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            try {

                val response = userApi.getUserInfo()

                if (response.isSuccessful && response.body()?.status == "success") {
                    Log.d("UserViewModel", "✅ API Response: ${response.body()}") // ✅ Log success
                    _userData.postValue(response.body()?.user)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e("UserViewModel", "❌ API Error: $errorResponse")
                    _userData.postValue(null)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "❌ Exception fetching user data: ${e.localizedMessage}")
                _userData.postValue(null)
            }
        }
    }
}

