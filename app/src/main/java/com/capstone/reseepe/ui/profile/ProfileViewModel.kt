package com.capstone.reseepe.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.pref.UserModel
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.response.LoginResponse
import com.capstone.reseepe.data.response.ProfileResponse
import com.capstone.reseepe.data.response.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User> = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            try {
                if (token.isBlank()) {
                    _error.value = "Token is empty or invalid"
                    Log.d("ProfileViewModel", "Token is empty or invalid")
                    return@launch
                }

                Log.d("ProfileViewModel", "Token fetchProfile: $token")

                val response = ApiConfig.getApiService().getProfile("Bearer $token")
                if (!response.error) {
                    _profile.value = response.user
                } else {
                    _error.value = response.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch profile: ${e.message}"
                Log.e("ProfileViewModel", "Failed to fetch profile: ${e.message}")
            }
        }
    }



    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}