package ru.woodymsk.socialapp.domain.post

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.post.model.PostEntity

interface PostRepository {

    suspend fun getAllPostList(): List<PostEntity>
    suspend fun getPagedPostList(): Flow<PagingData<PostEntity>>
    suspend fun getPostById(id: String): PostEntity
    suspend fun like(id: String): PostEntity
    suspend fun deleteLike(id: String): PostEntity

}