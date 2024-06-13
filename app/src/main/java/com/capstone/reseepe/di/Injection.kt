package com.capstone.reseepe.di

import android.content.Context
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.pref.UserPreference
import com.capstone.reseepe.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}