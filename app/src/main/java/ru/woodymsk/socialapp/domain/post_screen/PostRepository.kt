package ru.woodymsk.socialapp.domain.post_screen

import ru.woodymsk.socialapp.data.dto.PostDTO

interface PostRepository {

    suspend fun getAllPostList(): List<PostDTO>

}