package com.capstone.reseepe.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.api.ApiService
import com.capstone.reseepe.data.pref.UserPreference
import com.capstone.reseepe.data.response.ProfileResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ProfileRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getProfileUser() : ProfileResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        Log.d("ProfileRepository", "Token yang request getProfileUser: $token")
        return ApiConfig.getApiService().getProfile(token)
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
