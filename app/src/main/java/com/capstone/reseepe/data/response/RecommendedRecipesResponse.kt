package com.capstone.reseepe.data.response

import com.google.gson.annotations.SerializedName

data class RecommendedRecipesResponse(

	@field:SerializedName("recommendedRecipes")
	val recommendedRecipes: List<RecommendedRecipesItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class RecommendedRecipesItem(

	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("photoUrl")
	val photoUrl: Any,

	@field:SerializedName("instructions")
	val instructions: List<String>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ingredients")
	val ingredients: List<String>,

	@field:SerializedName("id")
	val id: Int
)
