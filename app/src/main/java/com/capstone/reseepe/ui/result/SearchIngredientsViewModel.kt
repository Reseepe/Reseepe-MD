package com.capstone.reseepe.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.response.IngredientListItem
import kotlinx.coroutines.launch

class SearchIngredientsViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<IngredientListItem>>()
    val searchResults: LiveData<List<IngredientListItem>> = _searchResults

    fun searchIngredients(query: String) {
        viewModelScope.launch {
            try {
                val response = recipeRepository.searchIngredientsByParams(query)
                _searchResults.value = response.ingredientList
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            }
        }
    }
}