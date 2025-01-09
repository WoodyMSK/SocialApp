package ru.woodymsk.socialapp.data.event.mapper

import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.data.event.model.EventDTO
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import javax.inject.Inject

class EventMapper @Inject constructor() {

    @Inject
    lateinit var auth: AppAuth

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
            likeOwnerIds = it.likeOwnerIds.orEmpty(),
            likedByMe = it.likedByMe.orFalse(),
            likes = it.likeOwnerIds.orEmpty().size,
            speakerIds = it.speakerIds.orEmpty(),
            participantsIds= it.participantsIds.orEmpty(),
            participatedByMe = it.participatedByMe.orFalse(),
            attachment = it.attachment,
            ownedByMe = it.authorId == auth.authStateFlow.value.id,
        )
    }
}