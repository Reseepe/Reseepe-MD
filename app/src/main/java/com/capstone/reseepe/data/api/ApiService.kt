package com.capstone.reseepe.data.api

import com.capstone.reseepe.data.response.EditProfileResponse
import com.capstone.reseepe.data.response.LoginResponse
import com.capstone.reseepe.data.response.ProfileResponse
import com.capstone.reseepe.data.response.RegisterResponse
import com.capstone.reseepe.data.response.ResetPasswordResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

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
}