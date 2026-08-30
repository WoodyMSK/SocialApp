package ru.woodymsk.socialapp.domain.common.model

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentMetadata(
    val title: String?,
    val artist: String?,
    val album: String?,
    val duration: String?,
    val author: String?,
    val composer: String?,
    val genre: String?,
)