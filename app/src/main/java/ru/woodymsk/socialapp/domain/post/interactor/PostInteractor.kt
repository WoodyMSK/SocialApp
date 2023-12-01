package ru.woodymsk.socialapp.domain.post.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.domain.post.mapper.PostMapper
import ru.woodymsk.socialapp.domain.post.model.Post
import javax.inject.Inject

class PostInteractor @Inject constructor(
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
) {

    suspend fun getPagedPostList(): Flow<PagingData<Post>> =
        postMapper.mapPostsFromEntity(postRepository.getPagedPostList())

    suspend fun onLikeButtonClick(postId: Int, likedByMe: Boolean) {
        if (likedByMe) {
            postRepository.deleteLike(postId.toString())
        } else {
            postRepository.like(postId.toString())
        }
    }

    suspend fun createPost(post: Post, upload: MediaUpload?) {
        if (upload == null) {
            postRepository.createPost(postMapper.mapSinglePostToEntity(post))
        } else {
            postRepository.createPostWithAttachment(
                postEntity = postMapper.mapSinglePostToEntity(post),
                upload = upload,
            )
        }
    }
}