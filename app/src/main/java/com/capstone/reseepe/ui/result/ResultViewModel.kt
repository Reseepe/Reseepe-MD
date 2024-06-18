package com.capstone.reseepe.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.model.IngredientItem
import com.capstone.reseepe.data.model.ProfileModel
import com.capstone.reseepe.data.repository.ProfileRepository
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.response.EditProfileResponse
import com.capstone.reseepe.data.response.PostBookmarkResponse
import com.capstone.reseepe.data.response.ResetPasswordResponse
import com.capstone.reseepe.data.response.ScanResultResponse
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: UserRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _ingredientList = MutableLiveData<MutableList<String>>()
    val ingredientList: LiveData<MutableList<String>> = _ingredientList

    private val _scanResultResponse = MutableLiveData<ScanResultResponse>()
    val scanResultResponse: LiveData<ScanResultResponse> = _scanResultResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _postBookmarkResponse = MutableLiveData<PostBookmarkResponse>()
    val postBookmarkResponse: LiveData<PostBookmarkResponse> = _postBookmarkResponse

    init {
        val mutableIngredients = mutableListOf("Tomato", "Chicken", "Mayonnaise", "Rice", "Spinach", "Salt")
        _ingredientList.value = mutableIngredients

        val ingredientObjects: List<IngredientItem> = mutableIngredients.map { ingredientName ->
            IngredientItem(name = ingredientName)
        }

        getRecipe(ingredientObjects)
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
