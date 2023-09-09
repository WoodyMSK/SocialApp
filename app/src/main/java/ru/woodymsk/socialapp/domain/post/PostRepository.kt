package ru.woodymsk.socialapp.domain.post

import androidx.paging.PagingData
import ru.woodymsk.socialapp.data.post.model.PostDAO

interface PostRepository {

    suspend fun getAllPostList(): List<PostDAO>
    suspend fun getPagedPostList(): List<PagingData<PostDAO>>

}