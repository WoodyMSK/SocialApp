package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.woodymsk.socialapp.data.post.model.PostDTO
import ru.woodymsk.socialapp.domain.profile.model.User

interface ProfileService {

    @GET("api/users/{user_id}")
    suspend fun getProfileData(
        @Path("user_id") id: String,
    ): Response<User>

    @GET("api/my/wall")
    suspend fun getProfilePostList(): Response<List<PostDTO>>
}