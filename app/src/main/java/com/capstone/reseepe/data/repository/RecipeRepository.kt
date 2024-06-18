package com.capstone.reseepe.data.repository

import android.util.Log
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.api.ApiService
import com.capstone.reseepe.data.model.IngredientItem
import com.capstone.reseepe.data.model.ProfileModel
import com.capstone.reseepe.data.model.RecipeRequest
import com.capstone.reseepe.data.pref.UserPreference
import com.capstone.reseepe.data.response.EditProfileResponse
import com.capstone.reseepe.data.response.GetBookmarkResponse
import com.capstone.reseepe.data.response.PostBookmarkResponse
import com.capstone.reseepe.data.response.ProfileResponse
import com.capstone.reseepe.data.response.ResetPasswordResponse
import com.capstone.reseepe.data.response.ScanResultResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RecipeRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getRecipe(ingredientList: List<IngredientItem>): ScanResultResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        val response = ApiConfig.getApiService().getRecipes(token, RecipeRequest(ingredientList))
        return response
    }

    suspend fun getBookmarked(): GetBookmarkResponse{
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        val response = ApiConfig.getApiService().getBookmark(token)
        return response
    }

    suspend fun postBookmark(idRecipe: Int): PostBookmarkResponse{
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        val response = ApiConfig.getApiService().bookmarkRecipe(token, idRecipe)
        return response
    }

    companion object {
        private const val TAG = "RecipeRepository"

        @Volatile
        private var instance: RecipeRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ) : RecipeRepository = instance ?: synchronized(this) {
            instance ?: RecipeRepository(apiService, userPreference)
        }.also { instance = it }
    }
}