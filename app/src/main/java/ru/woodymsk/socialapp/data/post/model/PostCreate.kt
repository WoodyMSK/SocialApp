package ru.woodymsk.socialapp.data.post.model

import ru.woodymsk.socialapp.data.model.Attachment

data class PostCreate(
    val id: Int,
    val content: String,
    val attachment: Attachment?,
)