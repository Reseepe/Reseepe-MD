package com.capstone.reseepe.di

import android.content.Context
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.pref.UserPreference
import com.capstone.reseepe.data.pref.dataStore
import com.capstone.reseepe.data.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return ProfileRepository.getInstance(apiService, pref)
    }
}