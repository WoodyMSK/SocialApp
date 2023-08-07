package ru.woodymsk.socialapp.domain.post.mapper

import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.data.post.model.PostDAO
import javax.inject.Inject

class PostMapper @Inject constructor() {

    fun mapPostToDao(items: List<Post>): List<PostDAO> = items.map {
        PostDAO(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            content = it.content,
            published = it.published,
            likeOwnerIds = it.likeOwnerIds,
            likedByMe = it.likedByMe,
            attachment = it.attachment
        )
    }

    fun mapPostFromDao(items: List<PostDAO>): List<Post> = items.map {
        Post(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            content = it.content,
            published = it.published,
            likeOwnerIds = it.likeOwnerIds,
            likedByMe = it.likedByMe,
            attachment = it.attachment
        )
    }
}