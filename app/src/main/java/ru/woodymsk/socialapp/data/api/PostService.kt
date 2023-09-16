package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.woodymsk.socialapp.data.post.model.PostDTO

interface PostService {

    @GET("api/posts")
    suspend fun getAllPostList(): Response<List<PostDTO>>

    @GET("api/posts/{post_id}/before")
    suspend fun getBeforePost(
        @Path("post_id") id: String,
        @Query("count") count: Int,
    ): Response<List<PostDTO>>

    @GET("api/posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<PostDTO>>
}