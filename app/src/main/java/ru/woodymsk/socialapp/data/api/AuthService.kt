package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.woodymsk.socialapp.data.auth.model.Token

interface AuthService {

    @FormUrlEncoded
    @POST("api/users/authentication")
    suspend fun authUser(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<Token>

    @FormUrlEncoded
    @POST("/api/users/registration/")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): Response<Token>

}