package ru.woodymsk.socialapp.domain.post_screen

import ru.woodymsk.socialapp.data.model.PostsItem

interface PostRepository {
    val posts: List<PostsItem>

    suspend fun getAllPostList()
}