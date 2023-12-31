package ru.woodymsk.socialapp.data.post.mapper

import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.post.model.PostCreate
import ru.woodymsk.socialapp.data.post.model.PostDTO
import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import javax.inject.Inject

class PostMapper @Inject constructor() {

    @Inject
    lateinit var auth: AppAuth

    fun mapToEntity(items: List<PostDTO>): List<PostEntity> = items.map {
        PostEntity(
            id = it.id.orZero(),
            authorId = it.authorId.orZero(),
            author = it.author.orEmpty(),
            authorAvatar = it.authorAvatar,
            content = it.content.orEmpty(),
            published = it.published.orEmpty(),
            likeOwnerIds = it.likeOwnerIds.orEmpty(),
            likedByMe = it.likedByMe.orFalse(),
            attachment = it.attachment,
            likes = it.likeOwnerIds.orEmpty().size,
            ownedByMe = it.authorId == auth.authStateFlow.value.id,
        )
    }

    fun mapSinglePostToEntity(item: PostDTO): PostEntity =
        PostEntity(
            id = item.id.orZero(),
            authorId = item.authorId.orZero(),
            author = item.author.orEmpty(),
            authorAvatar = item.authorAvatar,
            content = item.content.orEmpty(),
            published = item.published.orEmpty(),
            likeOwnerIds = item.likeOwnerIds.orEmpty(),
            likedByMe = item.likedByMe.orFalse(),
            attachment = item.attachment,
            likes = item.likeOwnerIds.orEmpty().size,
            ownedByMe = item.authorId == auth.authStateFlow.value.id,
        )

    fun mapSinglePostToCreate(item: PostEntity): PostCreate =
        PostCreate(
            id = item.id,
            content = item.content,
            attachment = item.attachment,
        )
}