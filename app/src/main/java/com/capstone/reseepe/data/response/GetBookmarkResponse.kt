package com.capstone.reseepe.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetBookmarkResponse(

	@field:SerializedName("bookmarkedRecipes")
	val bookmarkedRecipes: List<BookmarkedRecipesItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class BookmarkedRecipesItem(

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("isBookmarked")
	val isBookmarked: Boolean? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("instructions")
	val instructions: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("missingIngredients")
	val missingIngredients: List<String?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String?>? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Parcelable
