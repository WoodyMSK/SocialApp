package ru.woodymsk.socialapp.domain.post

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.model.Media
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.data.post.model.PostEntity

interface PostRepository {

    suspend fun getAllPostList(): List<PostEntity>
    suspend fun getPagedPostList(): Flow<PagingData<PostEntity>>
    suspend fun getPostById(id: String): PostEntity
    suspend fun like(id: String): PostEntity
    suspend fun deleteLike(id: String): PostEntity
    suspend fun createPost(postEntity: PostEntity)
    suspend fun createPostWithAttachment(postEntity: PostEntity, upload: MediaUpload)
    suspend fun uploadMedia(upload: MediaUpload): Media
    suspend fun removeAllDbPosts()
    suspend fun removePostById(id: String)

}