package ru.woodymsk.socialapp.domain.post

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.post.model.PostDAO

interface PostRepository {

    suspend fun getAllPostList(): List<PostDAO>
    suspend fun getPagedPostList(): Flow<PagingData<PostDAO>>

}