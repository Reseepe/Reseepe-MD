package com.capstone.reseepe.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.model.IngredientItem
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.response.PostBookmarkResponse
import com.capstone.reseepe.data.response.ScanIngredientResponse
import com.capstone.reseepe.data.response.ScanResultResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ResultViewModel(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _ingredientList = MutableLiveData<MutableList<String>>()
    val ingredientList: LiveData<MutableList<String>> = _ingredientList

    private val _scanIngredientResponse = MutableLiveData<ScanIngredientResponse>()
    val scanIngredientResponse: LiveData<ScanIngredientResponse> = _scanIngredientResponse

    private val _scanResultResponse = MutableLiveData<ScanResultResponse>()
    val scanResultResponse: LiveData<ScanResultResponse> = _scanResultResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isScanning = MutableLiveData<Boolean>()
    val isScanning: LiveData<Boolean> = _isScanning

    private val _postBookmarkResponse = MutableLiveData<PostBookmarkResponse>()
    val postBookmarkResponse: LiveData<PostBookmarkResponse> = _postBookmarkResponse

    fun getIngredients(photo: MultipartBody.Part) {
        viewModelScope.launch {
            _isScanning.value = true
            try {
                val response = recipeRepository.getIngredients(photo)
                _scanIngredientResponse.value = response
                _isScanning.value =  false
                response.ingredientList?.let { ingredientItems ->
                    val ingredientNames = ingredientItems.mapNotNull { it?.name }
                    Log.d("ResultViewModel", "Ingredients from API: $ingredientNames")
                    _ingredientList.value = ingredientNames.toMutableList()
                    Log.d("ResultViewModel", "Ingredients from var ingredientList: $ingredientList.value")
                    updateRecipeList()
                }
            } catch (e: Exception) {
                Log.e("ResultViewModel", "Error getting the Ingredients: ${e.message}")
                _scanIngredientResponse.value = ScanIngredientResponse(error = true, message = e.message)
                _isScanning.value = false
            }
        }
    }

    private fun updateRecipeList() {
        val ingredientObjects: List<IngredientItem> = _ingredientList.value?.map { ingredientName ->
            IngredientItem(name = ingredientName)
        } ?: emptyList()

        getRecipe(ingredientObjects)
    }

    fun removeIngredient(ingredient: String) {
        val currentList = _ingredientList.value ?: mutableListOf()
        currentList.remove(ingredient)
        _ingredientList.value = currentList
        updateRecipeList()
    }

    fun addIngredient(ingredient: String) {
        val currentList = _ingredientList.value ?: mutableListOf()
        currentList.add(ingredient)
        _ingredientList.value = currentList
        updateRecipeList()
    }


    fun getRecipe(ingredientList: List<IngredientItem>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = recipeRepository.getRecipe(ingredientList)
                _scanResultResponse.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("ResultViewModel", "Error getting the recipes: ${e.message}")
                _scanResultResponse.value = ScanResultResponse(error = true, message = e.message)
                _isLoading.value = false
            }
        }
    }

    fun postBookmark(idRecipe: Int) {
        viewModelScope.launch {
            try {
                val response = recipeRepository.postBookmark(idRecipe)
                _postBookmarkResponse.value = response
            } catch (e: Exception) {
                Log.e("ResultViewModel", "Error bookmarking recipes: ${e.message}")
                _postBookmarkResponse.value = PostBookmarkResponse(error = true, message = e.message)
            }
        }
    }
}
