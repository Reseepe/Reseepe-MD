package com.capstone.reseepe.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class User(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String
)
