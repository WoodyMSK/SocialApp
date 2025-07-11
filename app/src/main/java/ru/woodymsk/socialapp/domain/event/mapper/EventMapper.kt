package ru.woodymsk.socialapp.domain.event.mapper

import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.domain.convertDateToIsoFormat
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import javax.inject.Inject

class EventMapper @Inject constructor() {

    fun mapEventFromDao(items: List<EventDAO>): List<Event> = items.map {
        Event(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            content = it.content,
            datetime = it.datetime,
            published = it.published,
            coords = it.coords,
            type = it.type,
            likeOwnerIds = it.likeOwnerIds,
            likedByMe = it.likedByMe.orFalse(),
            likes = it.likeOwnerIds.size,
            speakerIds = it.speakerIds,
            participantsIds = it.participantsIds,
            participatedByMe = it.participatedByMe,
            attachment = it.attachment,
            link = it.link,
            ownedByMe = it.ownedByMe,
            users = it.users,
        )
    }

    fun mapSingleEventToDAO(item: Event): EventDAO = EventDAO(
        id = item.id.orZero(),
        authorId = item.authorId.orZero(),
        author = item.author,
        authorJob = item.authorJob,
        authorAvatar = item.authorAvatar,
        content = item.content,
        datetime = convertDateToIsoFormat(item.datetime),
        published = item.published,
        coords = item.coords,
        type = item.type,
        likeOwnerIds = item.likeOwnerIds,
        likedByMe = item.likedByMe.orFalse(),
        likes = item.likeOwnerIds.size,
        speakerIds = item.speakerIds,
        participantsIds = item.participantsIds,
        participatedByMe = item.participatedByMe.orFalse(),
        attachment = item.attachment,
        link = item.link,
        ownedByMe = item.ownedByMe,
        users = item.users,
    )
}