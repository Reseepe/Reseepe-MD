package com.capstone.reseepe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.reseepe.data.api.ApiService

class HomeViewModel(private val apiService: ApiService) : ViewModel() {
//
//    private val _recommendedRecipes = MutableLiveData<List<RecipeItem>>()
//    val recommendedRecipes: LiveData<List<RecipeItem>> = _recommendedRecipes
//
//    fun fetchRecommendedRecipes(token: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.getRecommendedRecipes(token)
//                if (!response.error) {
//                    _recommendedRecipes.value = response.recommendedRecipes.map { item ->
//                        RecipeItem(
//                            id = item.id,
//                            name = item.name,
//                            duration = item.duration,
//                            photoUrl = item.photoUrl as String, // Assuming photoUrl is a string
//                            description = item.description,
//                            ingredients = item.ingredients,
//                            instructions = item.instructions
//                        )
//                    }
//                } else {
//                    // Handle error
//                }
//            } catch (e: Exception) {
//                // Handle exception
//            }
//        }
//    }
}
