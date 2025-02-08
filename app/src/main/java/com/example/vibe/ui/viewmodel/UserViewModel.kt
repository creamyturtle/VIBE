package com.example.vibe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.MoreUserData
import com.example.vibe.network.UserApi
import com.example.vibe.utils.SessionManager
import kotlinx.coroutines.launch

class UserViewModel(private val userApi: UserApi, private val sessionManager: SessionManager) : ViewModel() {

    private val _userData = MutableLiveData<MoreUserData?>()
    val userData: LiveData<MoreUserData?> = _userData

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

