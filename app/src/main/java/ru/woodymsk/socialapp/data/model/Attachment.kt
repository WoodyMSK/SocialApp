package ru.woodymsk.socialapp.data.model

data class Attachment(
    val url: String,
    val type: AttachmentType,
)

enum class AttachmentType {
    IMAGE,
    AUDIO,
    VIDEO,
}