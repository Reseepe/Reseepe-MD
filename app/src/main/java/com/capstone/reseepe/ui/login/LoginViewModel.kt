package com.capstone.reseepe.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.pref.UserModel
import com.capstone.reseepe.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().login(email, password)
                _loginResult.value = response
                if (response.error == true) {
                    _loginResult.value = response
                    _isLoading.value = false
                } else {
                    response.loginResult?.let {
                        if (!it.token.isNullOrEmpty()) {
                            saveSession(UserModel(email, it.token, true))
                            _isLoading.value = false
                        } else {
                            _loginResult.value = LoginResponse(error = true, message = "Token is null or empty")
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResponse(error = true, message = e.message)
                _isLoading.value = false
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}