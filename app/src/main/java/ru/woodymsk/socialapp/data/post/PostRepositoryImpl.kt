package ru.woodymsk.socialapp.data.post

import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.post.model.PostDAO
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.error.AppError
import withContextIO
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val postMapper: PostMapper,
) : PostRepository {

    override suspend fun getAllPostList(): List<PostDAO> = withContextIO {
        val response = postService.getAllPostList()
        postMapper.mapToDao(response.body() ?: throw AppError.ApiError(response.message()))
    }
}