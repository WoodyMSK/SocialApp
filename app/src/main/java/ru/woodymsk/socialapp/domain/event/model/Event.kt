package ru.woodymsk.socialapp.domain.event.model

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.EventType

data class Event(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val type: EventType?,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val likes: Int,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val ownedByMe: Boolean,
)