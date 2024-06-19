package com.capstone.reseepe.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class TopFiveRecommendationResponse(

	@field:SerializedName("recommendedList")
	val recommendedList: List<RecommendedListItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class RecommendedListItem(

	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("isBookmarked")
	val isBookmarked: Boolean,

	@field:SerializedName("photoUrl")
	val photoUrl: String?,

	@field:SerializedName("instructions")
	val instructions: List<String>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String?,

	@field:SerializedName("ingredients")
	val ingredients: List<String>,

	@field:SerializedName("id")
	val id: Int
) : Parcelable
