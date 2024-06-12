package com.capstone.reseepe.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.response.RegisterResponse
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, birthday: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.register(name, email, birthday, password)
                _registerResponse.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _registerResponse.value = RegisterResponse(error = true, message = e.message)
                _isLoading.value = false
            }
        }
    }
}