package ru.woodymsk.socialapp.data.event.model

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.data.model.UserPreview

data class EventDTO(
    val id: Int?,
    val authorId: Int?,
    val author: String?,
    val authorJob: String?,
    val authorAvatar: String?,
    val content: String?,
    val datetime: String?,
    val published: String?,
    val coords: Coords?,
    val type: EventType?,
    val likeOwnerIds: List<Int>?,
    val likedByMe: Boolean?,
    val speakerIds: List<Int>?,
    val participantsIds: List<Int>?,
    val participatedByMe: Boolean?,
    val attachment: Attachment?,
    val link: String?,
    val users: Map<Int, UserPreview>,
)