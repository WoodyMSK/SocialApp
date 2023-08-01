package ru.woodymsk.socialapp.data.post_screen

import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.dto.PostDTO
import ru.woodymsk.socialapp.domain.post_screen.PostRepository
import ru.woodymsk.socialapp.error.AppError
import withContextIO
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService
) : PostRepository {

    override suspend fun getAllPostList(): List<PostDTO> = withContextIO {
        val response = postService.getAllPostList()
        response.body() ?: throw AppError.ApiError(response.message())
    }
}