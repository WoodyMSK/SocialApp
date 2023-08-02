package ru.woodymsk.socialapp.domain.post

import ru.woodymsk.socialapp.data.post.model.PostDAO

interface PostRepository {

    suspend fun getAllPostList(): List<PostDAO>

}