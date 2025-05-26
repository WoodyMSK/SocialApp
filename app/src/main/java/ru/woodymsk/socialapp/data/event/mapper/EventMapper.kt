package ru.woodymsk.socialapp.data.event.mapper

import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.data.event.model.EventDTO
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventMapper @Inject constructor() {

    @Inject
    lateinit var auth: AppAuth

    fun mapToDao(items: List<EventDTO>): List<EventDAO> = items.map {
        EventDAO(
            id = it.id.orZero(),
            authorId = it.authorId.orZero(),
            author = it.author.orEmpty(),
            authorJob = it.authorJob,
            authorAvatar = it.authorAvatar,
            content = it.content.orEmpty(),
            datetime = it.datetime.orEmpty(),
            published = it.published.orEmpty(),
            coords = it.coords,
            type = it.type,
            likeOwnerIds = it.likeOwnerIds.orEmpty(),
            likedByMe = it.likedByMe.orFalse(),
            likes = it.likeOwnerIds.orEmpty().size,
            speakerIds = it.speakerIds.orEmpty(),
            participantsIds = it.participantsIds.orEmpty(),
            participatedByMe = it.participatedByMe.orFalse(),
            attachment = it.attachment,
            link = it.link,
            ownedByMe = it.authorId == auth.authStateFlow.value.id,
            users = it.users,
        )
    }

    fun mapToDto(item: EventDAO): EventDTO =
        EventDTO(
            id = item.id,
            authorId = item.authorId,
            author = item.author,
            authorJob = item.authorJob,
            authorAvatar = item.authorAvatar,
            content = item.content,
            datetime = item.datetime,
            published = OffsetDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            coords = item.coords,
            type = item.type,
            likeOwnerIds = item.likeOwnerIds,
            likedByMe = item.likedByMe,
            speakerIds = item.speakerIds,
            participantsIds = item.participantsIds,
            participatedByMe = item.participatedByMe,
            attachment = item.attachment,
            link = item.link,
            users = item.users,
        )
}