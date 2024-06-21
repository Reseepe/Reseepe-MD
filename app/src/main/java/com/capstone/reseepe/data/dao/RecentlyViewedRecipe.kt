package com.capstone.reseepe.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "recently_viewed_recipes")
@Parcelize
data class RecentlyViewedRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: Int,
    val name: String,
    val photoUrl: String?,
    val duration: Int,
    val ingredientsCount: Int,
    val ingredients: List<String>,
    val instructions: List<String>,
    val isBookmarked: Boolean?,
    val description: String?,
    val hasMissingIngredients: Boolean,
    val missingIngredients: List<String?>?,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable
