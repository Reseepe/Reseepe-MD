package com.capstone.reseepe.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SearchIngredientsResponse(

	@field:SerializedName("found")
	val found: Int,

	@field:SerializedName("ingredientList")
	val ingredientList: List<IngredientListItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class IngredientListItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
) : Parcelable
