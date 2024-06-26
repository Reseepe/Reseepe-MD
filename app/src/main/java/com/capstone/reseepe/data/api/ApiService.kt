package com.capstone.reseepe.data.api

import com.capstone.reseepe.data.model.IngredientItem
import com.capstone.reseepe.data.model.RecipeRequest
import com.capstone.reseepe.data.response.EditProfileResponse
import com.capstone.reseepe.data.response.GetBookmarkResponse
import com.capstone.reseepe.data.response.LoginResponse
import com.capstone.reseepe.data.response.PostBookmarkResponse
import com.capstone.reseepe.data.response.ProfileResponse
import com.capstone.reseepe.data.response.RegisterResponse
import com.capstone.reseepe.data.response.ResetPasswordResponse
import com.capstone.reseepe.data.response.ScanIngredientResponse
import com.capstone.reseepe.data.response.ScanResultResponse
import com.capstone.reseepe.data.response.SearchIngredientsResponse
import com.capstone.reseepe.data.response.TopFiveRecommendationResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("birthday") birthday: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @GET("recommended")
    suspend fun getTopFiveRecommendationRecipes(
        @Header("Authorization") token: String
    ): TopFiveRecommendationResponse

    @FormUrlEncoded
    @POST("changespass")
    suspend fun changePass(
        @Header("Authorization") token: String,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
    ): ResetPasswordResponse

    @FormUrlEncoded
    @PUT("profile/edit/")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("birthday") birthday: String,
    ): EditProfileResponse

    @POST("recipes")
    suspend fun getRecipes(
        @Header("Authorization") token: String,
        @Body recipeRequest: RecipeRequest
    ): ScanResultResponse

    @GET("bookmarked")
    suspend fun getBookmark(
        @Header("Authorization") token: String
    ): GetBookmarkResponse

    @POST("bookmark/{id}")
    suspend fun bookmarkRecipe(
        @Header("Authorization") token: String,
        @Path("id") recipeId: Int
    ): PostBookmarkResponse

    @POST("unbookmark/{id}")
    suspend fun unbookmarkRecipe(
        @Header("Authorization") token: String,
        @Path("id") recipeId: Int
    ): PostBookmarkResponse

    @GET("search")
    suspend fun searchIngredientsByParams(
        @Query("q") query: String
    ): SearchIngredientsResponse

    @Multipart
    @POST("ingredient")
    suspend fun getIngredients(
        @Part photo: MultipartBody.Part
    ): ScanIngredientResponse
}