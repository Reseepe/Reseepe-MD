package com.capstone.reseepe.data.response

import com.google.gson.annotations.SerializedName

data class ScanIngredientResponse(

    @field:SerializedName("ingredientList")
    val ingredientList: List<ScannedIngredientItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ScannedIngredientItem(

    @field:SerializedName("name")
    val name: String? = null
)
