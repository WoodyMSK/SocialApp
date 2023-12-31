package ru.woodymsk.socialapp.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.woodymsk.socialapp.data.model.Media
import ru.woodymsk.socialapp.data.post.model.PostCreate
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

    @GET("api/posts/{post_id}")
    suspend fun getPostById(
        @Path("post_id") id: String,
    ): Response<PostDTO>

    @POST("api/posts/{post_id}/likes")
    suspend fun like(
        @Path("post_id") id: String,
    ): Response<PostDTO>

    @DELETE("api/posts/{post_id}/likes")
    suspend fun deleteLike(
        @Path("post_id") id: String,
    ): Response<PostDTO>

    @POST("api/posts")
    suspend fun createPost(
        @Body postCreate: PostCreate
    ): Response<PostDTO>

    @Multipart
    @POST("api/media")
    suspend fun uploadMedia(
        @Part media: MultipartBody.Part
    ): Response<Media>

    @DELETE("api/posts/{post_id}")
    suspend fun removePostById(
        @Path("post_id") id: String,
    ): Response<Unit>
}