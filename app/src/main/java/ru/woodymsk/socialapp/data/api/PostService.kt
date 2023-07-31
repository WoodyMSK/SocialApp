package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.woodymsk.socialapp.data.dto.PostDTO

interface PostService {

    @GET("api/posts")
    suspend fun getAllPostList(): Response<List<PostDTO>>

}