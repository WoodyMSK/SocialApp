package ru.woodymsk.socialapp.data.post.model

import ru.woodymsk.socialapp.data.model.Attachment

data class PostDAO(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val attachment: Attachment?,
)