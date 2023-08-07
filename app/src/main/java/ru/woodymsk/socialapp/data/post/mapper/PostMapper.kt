package ru.woodymsk.socialapp.data.post.mapper

import ru.woodymsk.socialapp.data.post.model.PostDTO
import ru.woodymsk.socialapp.data.post.model.PostDAO
import javax.inject.Inject

class PostMapper @Inject constructor() {

    fun mapToDao(items: List<PostDTO>): List<PostDAO> = items.map {
        PostDAO(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            content = it.content,
            published = it.published,
            likeOwnerIds = it.likeOwnerIds,
            likedByMe = it.ownedByMe,
            attachment = it.attachment
        )
    }

    fun mapFromDao(items: List<PostDAO>): List<PostDTO> = items.map {
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
            ownedByMe = false,
        )
    }
}