package ru.woodymsk.socialapp.domain.event.model

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.EventType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Event(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")),
    val published: String = "",
    val type: EventType? = EventType.OFFLINE,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val speakerIds: List<Int> = emptyList(),
    val participantsIds: List<Int> = emptyList(),
    val participatedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
)