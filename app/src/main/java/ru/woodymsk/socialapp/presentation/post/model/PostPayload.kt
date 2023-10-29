package ru.woodymsk.socialapp.presentation.post.model

data class PostPayload(
    val id: Int? = null,
    val liked: Boolean? = null,
    val likedByMe: Int? = null,
)