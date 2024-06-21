package com.capstone.reseepe.data.response

import com.google.gson.annotations.SerializedName

data class PostBookmarkResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
