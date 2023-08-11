package ru.woodymsk.socialapp.data.event.mapper

import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.data.event.model.EventDTO
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import javax.inject.Inject

class EventMapper @Inject constructor() {

    fun mapToDao(items: List<EventDTO>): List<EventDAO> = items.map {
        EventDAO(
            id = it.id.orZero(),
            authorId = it.authorId.orZero(),
            author = it.author.orEmpty(),
            authorAvatar = it.authorAvatar,
            content = it.content.orEmpty(),
            datetime = it.datetime.orEmpty(),
            published = it.published.orEmpty(),
            type = it.type,
            speakerIds = it.speakerIds.orEmpty(),
            participantsIds= it.participantsIds.orEmpty(),
            participatedByMe = it.participatedByMe.orFalse(),
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