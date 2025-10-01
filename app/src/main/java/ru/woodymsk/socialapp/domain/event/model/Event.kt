package ru.woodymsk.socialapp.domain.event.model

import kotlinx.serialization.Serializable
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.data.model.UserPreview
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Event(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")),
    val published: String = "",
    val coords: Coords? = null,
    val type: EventType? = EventType.OFFLINE,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val likes: String = "",
    val speakerIds: List<Int> = emptyList(),
    val participantsIds: List<Int> = emptyList(),
    val participatedByMe: Boolean = false,
    val participantsNumber: String = "",
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
    val link: String? = null,
    val users: Map<Int, UserPreview> = emptyMap(),
)