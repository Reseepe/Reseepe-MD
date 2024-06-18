package com.capstone.reseepe.ui.bookmarks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.reseepe.data.model.IngredientItem
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.data.response.GetBookmarkResponse
import com.capstone.reseepe.data.response.ScanResultResponse
import kotlinx.coroutines.launch

class BookmarksViewModel (private val repository: UserRepository, private val recipeRepository: RecipeRepository) : ViewModel() {


    private val _getBookmarkResponse = MutableLiveData<GetBookmarkResponse>()
    val getBookmarkResponse: LiveData<GetBookmarkResponse> = _getBookmarkResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    init {
        getBookmark()
    }

    fun getBookmark() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = recipeRepository.getBookmarked()
                _getBookmarkResponse.value = response
                _isLoading.value = false
                _isEmpty.value = response.bookmarkedRecipes?.isEmpty() == true
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Error getting the recipes: ${e.message}")
                _getBookmarkResponse.value = GetBookmarkResponse(error = true, message = e.message)
                _isLoading.value = false
            }
        }
    }


}