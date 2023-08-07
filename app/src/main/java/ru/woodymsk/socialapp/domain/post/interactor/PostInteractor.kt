package ru.woodymsk.socialapp.domain.post.interactor

import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.domain.post.mapper.PostMapper
import ru.woodymsk.socialapp.domain.post.PostRepository
import javax.inject.Inject

class PostInteractor @Inject constructor(
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
) {

    suspend fun getAllPostList(): List<Post> =
        postMapper.mapPostFromDao(postRepository.getAllPostList())
}