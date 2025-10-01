package ru.woodymsk.socialapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreview(
    val name: String,
    val avatar: String?,
)