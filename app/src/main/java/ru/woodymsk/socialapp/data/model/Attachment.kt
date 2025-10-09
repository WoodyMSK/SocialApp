package ru.woodymsk.socialapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val url: String,
    val type: AttachmentType?,
)

enum class AttachmentType {
    IMAGE,
    AUDIO,
    VIDEO,
}