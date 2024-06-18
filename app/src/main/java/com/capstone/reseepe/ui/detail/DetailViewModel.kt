package com.capstone.reseepe.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.model.IngredientItem
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.response.PostBookmarkResponse
import com.capstone.reseepe.data.response.ScanResultResponse
import kotlinx.coroutines.launch

class DetailViewModel (private val repository: UserRepository, private val recipeRepository: RecipeRepository) : ViewModel() {


    private val _postBookmarkResponse = MutableLiveData<PostBookmarkResponse>()
    val postBookmarkResponse: LiveData<PostBookmarkResponse> = _postBookmarkResponse

    fun postBookmark(idRecipe: Int) {
        viewModelScope.launch {
            try {
                val response = recipeRepository.postBookmark(idRecipe)
                _postBookmarkResponse.value = response
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error bookmarking recipes: ${e.message}")
                _postBookmarkResponse.value = PostBookmarkResponse(error = true, message = e.message)
            }
        }
    }

    fun postUnbookmark(idRecipe: Int){
        viewModelScope.launch {
            try {
                val response = recipeRepository.postUnbookmark(idRecipe)
                _postBookmarkResponse.value = response
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error unbookmarking recipes: ${e.message}")
                _postBookmarkResponse.value = PostBookmarkResponse(error = true, message = e.message)
            }
        }
    }


}