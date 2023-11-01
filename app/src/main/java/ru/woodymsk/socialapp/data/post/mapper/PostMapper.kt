package ru.woodymsk.socialapp.data.post.mapper

import ru.woodymsk.socialapp.data.post.model.PostDTO
import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import javax.inject.Inject

class PostMapper @Inject constructor() {

    fun mapToDao(items: List<PostDTO>): List<PostEntity> = items.map {
        PostEntity(
            id = it.id.orZero(),
            authorId = it.authorId.orZero(),
            author = it.author.orEmpty(),
            authorAvatar = it.authorAvatar,
            content = it.content.orEmpty(),
            published = it.published.orEmpty(),
            likeOwnerIds = it.likeOwnerIds.orEmpty(),
            likedByMe = it.likedByMe.orFalse(),
            attachment = it.attachment
        )
    }

    fun mapFromDao(items: List<PostEntity>): List<PostDTO> = items.map {
        PostDTO(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            authorJob = null,
            content = it.content,
            published = it.published,
            coords = null,
            link = null,
            likeOwnerIds = emptyList(),
            mentionIds = emptyList(),
            mentionedMe = false,
            likedByMe = false,
            attachment = it.attachment,
            ownedByMe = true,
        )
    }

    fun mapSinglePostToDao(item: PostDTO): PostEntity =
        PostEntity(
            id = item.id.orZero(),
            authorId = item.authorId.orZero(),
            author = item.author.orEmpty(),
            authorAvatar = item.authorAvatar,
            content = item.content.orEmpty(),
            published = item.published.orEmpty(),
            likeOwnerIds = item.likeOwnerIds.orEmpty(),
            likedByMe = item.likedByMe.orFalse(),
            attachment = item.attachment
        )
}