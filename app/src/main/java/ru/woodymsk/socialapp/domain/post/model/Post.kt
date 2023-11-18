package ru.woodymsk.socialapp.domain.post.model

import ru.woodymsk.socialapp.data.model.Attachment

data class Post(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String? = null,
    val content: String = "",
    val published: String = "",
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val likes: Int = 0,
)