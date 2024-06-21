package com.capstone.reseepe.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.model.ProfileModel
import com.capstone.reseepe.data.pref.UserModel
import com.capstone.reseepe.data.repository.ProfileRepository
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.response.EditProfileResponse
import com.capstone.reseepe.data.response.LoginResponse
import com.capstone.reseepe.data.response.ProfileResponse
import com.capstone.reseepe.data.response.ResetPasswordResponse

import com.capstone.reseepe.data.response.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: UserRepository, private val profileRepository: ProfileRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<ProfileModel>()
    val userProfile: LiveData<ProfileModel> = _userProfile

    private val _resetPasswordResult = MutableLiveData<ResetPasswordResponse>()
    val resetPasswordResult: LiveData<ResetPasswordResponse> = _resetPasswordResult

    private val _editProfileResult = MutableLiveData<EditProfileResponse>()
    val editProfileResult: LiveData<EditProfileResponse> = _editProfileResult

    fun editProfile(name: String, email: String, birthday: String) {
        viewModelScope.launch {
            try {
                val response = profileRepository.editProfile(name, email, birthday)
                _editProfileResult.value = response
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error edit profile: ${e.message}")
                _editProfileResult.value = EditProfileResponse(error = true, message = e.message ?: "Unknown error")
            }
        }
    }

    fun resetPassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = profileRepository.resetPassword(oldPassword, newPassword)
                _resetPasswordResult.value = response
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error resetting password: ${e.message}")
                _resetPasswordResult.value = ResetPasswordResponse(error = true, message = e.message ?: "Unknown error")
            }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val profileResponse = profileRepository.getProfileUser()
                _userProfile.value = profileResponse
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}