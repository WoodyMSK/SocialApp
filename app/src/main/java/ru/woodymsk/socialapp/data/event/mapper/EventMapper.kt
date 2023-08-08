package ru.woodymsk.socialapp.data.event.mapper

import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.data.event.model.EventDTO
import javax.inject.Inject

class EventMapper @Inject constructor() {

    fun mapToDao(items: List<EventDTO>): List<EventDAO> = items.map {
        EventDAO(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            content = it.content,
            datetime = it.datetime,
            published = it.published,
            type = it.type,
            speakerIds = it.speakerIds,
            participantsIds= it.participantsIds,
            participatedByMe = it.participatedByMe,
            attachment = it.attachment,
        )
    }

    fun mapToDto(items: List<EventDAO>): List<EventDTO> = items.map {
        EventDTO(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            authorJob = null,
            content = it.content,
            datetime = it.datetime,
            published = it.published,
            coords = null,
            type = it.type,
            likeOwnerIds = emptyList(),
            likedByMe = false,
            speakerIds = emptyList(),
            participantsIds = emptyList(),
            participatedByMe = true,
            attachment = it.attachment,
            link = null,
            ownedByMe = true,
        )
    }
}