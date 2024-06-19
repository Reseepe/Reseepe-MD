package com.capstone.reseepe.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.dao.RecentlyViewedRecipe
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.response.RecommendedListItem
import kotlinx.coroutines.launch

class HomeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _topFiveRecommendations = MutableLiveData<List<RecommendedListItem>>()
    val topFiveRecommendations: LiveData<List<RecommendedListItem>> = _topFiveRecommendations

    // LiveData untuk menyimpan daftar Recently Viewed Recipes
    val recentlyViewedRecipes: LiveData<List<RecentlyViewedRecipe?>?>? = recipeRepository.getRecentlyViewedRecipes()

    // Fungsi untuk menyimpan resep yang baru dilihat
    fun saveRecentlyViewedRecipe(recipe: RecentlyViewedRecipe) {
        viewModelScope.launch {
            recipeRepository.insertRecentlyViewedRecipe(recipe)
            deleteOldRecentlyViewedRecipes()
        }
    }

    // Fungsi untuk menghapus resep lama yang tidak termasuk dalam lima terakhir dilihat
    fun deleteOldRecentlyViewedRecipes() {
        viewModelScope.launch {
            recipeRepository.deleteOldRecentlyViewedRecipes()
        }
    }

    fun fetchTopFiveRecommendations() {
        viewModelScope.launch {
            try {
                val response = recipeRepository.getTopFiveRecommendation()
                _topFiveRecommendations.value = response.recommendedList
            } catch (e: Exception) {
                // Handle error
                Log.e(TAG, "Error fetching top five recommendations: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}

