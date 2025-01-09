package ru.woodymsk.socialapp.domain.event.mapper

import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.orFalse
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
            type = it.type,
            likeOwnerIds = it.likeOwnerIds,
            likedByMe = it.likedByMe.orFalse(),
            likes = it.likeOwnerIds.size,
            speakerIds = it.speakerIds,
            participantsIds= it.participantsIds,
            participatedByMe = it.participatedByMe,
            attachment = it.attachment,
            ownedByMe = it.ownedByMe,
        )
    }
}