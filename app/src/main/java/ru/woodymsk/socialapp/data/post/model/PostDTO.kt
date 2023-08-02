package ru.woodymsk.socialapp.data.post.model

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.Coords

data class PostDTO(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coords?,
    val link: String?,
    val likeOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
    val ownedByMe: Boolean,
)