package com.capstone.reseepe.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.capstone.reseepe.data.api.ApiConfig
import com.capstone.reseepe.data.api.ApiService
import com.capstone.reseepe.data.dao.RecentlyViewedRecipe
import com.capstone.reseepe.data.dao.RecentlyViewedRecipeDao
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
import com.capstone.reseepe.data.response.TopFiveRecommendationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RecipeRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val recentlyViewedRecipeDao: RecentlyViewedRecipeDao
) {

    // Fungsi untuk mendapatkan data Recently Viewed Recipes dari Room Database
    fun getRecentlyViewedRecipes(): LiveData<List<RecentlyViewedRecipe?>?>? {
        return recentlyViewedRecipeDao.getRecentlyViewedRecipes
    }

    // Fungsi untuk menyimpan resep yang baru dilihat ke dalam Room Database
    fun insertRecentlyViewedRecipe(recipe: RecentlyViewedRecipe) {
        CoroutineScope(Dispatchers.IO).launch {
            recentlyViewedRecipeDao.insertRecipe(recipe)
        }
    }

    // Fungsi untuk menghapus resep lama yang tidak termasuk dalam lima terakhir dilihat
    fun deleteOldRecentlyViewedRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            recentlyViewedRecipeDao.deleteOldRecipes()
        }
    }


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

    suspend fun getTopFiveRecommendation() : TopFiveRecommendationResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        val response = ApiConfig.getApiService().getTopFiveRecommendationRecipes(token)
        return response
    }

    suspend fun postUnbookmark(idRecipe: Int): PostBookmarkResponse{
        val user = runBlocking { userPreference.getSession().first() }
        val token = "Bearer ${user.token}"
        val response = ApiConfig.getApiService().unbookmarkRecipe(token, idRecipe)
        return response
    }

    companion object {
        private const val TAG = "RecipeRepository"

        @Volatile
        private var instance: RecipeRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            recentlyViewedRecipeDao: RecentlyViewedRecipeDao
        ) : RecipeRepository = instance ?: synchronized(this) {
            instance ?: RecipeRepository(apiService, userPreference, recentlyViewedRecipeDao)
        }.also { instance = it }
    }
}