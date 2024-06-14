package com.capstone.reseepe.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.api.ApiService
import com.capstone.reseepe.data.model.ProfileModel
import com.capstone.reseepe.data.pref.UserPreference
import com.capstone.reseepe.data.response.EditProfileResponse
import com.capstone.reseepe.data.response.ProfileResponse
import com.capstone.reseepe.data.response.ResetPasswordResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ProfileRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getProfileUser() : ProfileModel {
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        Log.d("ProfileRepository", "Token yang request getProfileUser: $token")
        val response = ApiConfig.getApiService().getProfile(token)
        return response.toProfileModel()
    }

    suspend fun resetPassword(oldPassword: String, newPassword: String): ResetPasswordResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        return ApiConfig.getApiService().changePass(token, oldPassword, newPassword)
    }

    suspend fun editProfile(name: String, email: String, birthday: String) : EditProfileResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        return ApiConfig.getApiService().editProfile(token, name, email, birthday)
    }

    companion object {
        private const val TAG = "ProfileRepository"

        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference) : ProfileRepository = instance ?: synchronized(this) {
            instance ?: ProfileRepository(apiService, userPreference)
            }.also { instance = it }
    }
}

private fun ProfileResponse.toProfileModel(): ProfileModel {
    return ProfileModel(
        name = this.user.name,
        email = this.user.email,
        userId = this.user.userId
    )
}