package com.capstone.reseepe.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.reseepe.data.repository.ProfileRepository
import com.capstone.reseepe.data.repository.RecipeRepository
import com.capstone.reseepe.data.repository.UserRepository
import com.capstone.reseepe.di.Injection
import com.capstone.reseepe.ui.bookmarks.BookmarksViewModel
import com.capstone.reseepe.ui.login.LoginViewModel
import com.capstone.reseepe.ui.main.MainViewModel
import com.capstone.reseepe.ui.profile.ProfileViewModel
import com.capstone.reseepe.ui.result.ResultViewModel
import com.capstone.reseepe.ui.signup.SignupViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val recipeRepository: RecipeRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, profileRepository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(userRepository, recipeRepository) as T
            }
            modelClass.isAssignableFrom(BookmarksViewModel::class.java) -> {
                BookmarksViewModel(userRepository, recipeRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val userRepository = Injection.provideRepository(context)
                    val profileRepository = Injection.provideProfileRepository(context)
                    val recipeRepository = Injection.provideRecipeRepository(context)
                    INSTANCE = ViewModelFactory(userRepository, profileRepository, recipeRepository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}