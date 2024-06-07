package com.capstone.reseepe.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.UserRepository
import com.capstone.reseepe.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}