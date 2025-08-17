package ru.woodymsk.socialapp.domain.event.mapper

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.domain.convertDateToIsoFormat
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import javax.inject.Inject

class EventMapper @Inject constructor() {

    fun mapEventFromEntity(items: Flow<PagingData<EventEntity>>): Flow<PagingData<Event>> = items.map {
        it.map { eventEntity ->
            Event(
                id = eventEntity.id,
                authorId = eventEntity.authorId,
                author = eventEntity.author,
                authorAvatar = eventEntity.authorAvatar,
                content = eventEntity.content,
                datetime = eventEntity.datetime,
                published = eventEntity.published,
                coords = eventEntity.coords,
                type = eventEntity.eventType,
                likeOwnerIds = eventEntity.likeOwnerIds,
                likedByMe = eventEntity.likedByMe.orFalse(),
                likes = eventEntity.likeOwnerIds.size,
                speakerIds = eventEntity.speakerIds,
                participantsIds = eventEntity.participantsIds,
                participatedByMe = eventEntity.participatedByMe,
                attachment = eventEntity.attachment,
                link = eventEntity.link,
                ownedByMe = eventEntity.ownedByMe,
                users = eventEntity.users,
            )
        }
    }

    fun mapSingleEventToDAO(item: Event): EventEntity = EventEntity(
        id = item.id.orZero(),
        authorId = item.authorId.orZero(),
        author = item.author,
        authorJob = item.authorJob,
        authorAvatar = item.authorAvatar,
        content = item.content,
        datetime = convertDateToIsoFormat(item.datetime),
        published = item.published,
        coords = item.coords,
        eventType = item.type,
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