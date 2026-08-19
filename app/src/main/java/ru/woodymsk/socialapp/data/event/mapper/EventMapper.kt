package ru.woodymsk.socialapp.data.event.mapper

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.awaitAll
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.data.event.model.EventDTO
import ru.woodymsk.socialapp.data.media.AudioMetadataExtractor
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.domain.common.model.AttachmentMetadata
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventMapper @Inject constructor(
    private val audioMetadataExtractor: AudioMetadataExtractor,
) {

    @Inject
    lateinit var auth: AppAuth

    suspend fun mapListDtoToListEntity(items: List<EventDTO>): List<EventEntity> = coroutineScope {
        items.map {
            async {
                EventEntity(
                    id = it.id.orZero(),
                    authorId = it.authorId.orZero(),
                    author = it.author.orEmpty(),
                    authorJob = it.authorJob,
                    authorAvatar = it.authorAvatar,
                    content = it.content.orEmpty(),
                    datetime = it.datetime.orEmpty(),
                    published = it.published.orEmpty(),
                    coords = it.coords,
                    eventType = it.type,
                    likeOwnerIds = it.likeOwnerIds.orEmpty(),
                    likedByMe = it.likedByMe.orFalse(),
                    likes = it.likeOwnerIds.orEmpty().size,
                    speakerIds = it.speakerIds.orEmpty(),
                    participantsIds = it.participantsIds.orEmpty(),
                    participatedByMe = it.participatedByMe.orFalse(),
                    attachment = it.attachment,
                    attachmentMetadata = it.attachment?.let { attachment -> getAttachmentMetadata(attachment) },
                    link = it.link,
                    ownedByMe = it.authorId == auth.authStateFlow.value.id,
                    users = it.users,
                )
            }
        }.awaitAll()
    }

    suspend fun mapDtoToEntity(item: EventDTO): EventEntity =
        EventEntity(
            id = item.id.orZero(),
            authorId = item.authorId.orZero(),
            author = item.author.orEmpty(),
            authorJob = item.authorJob,
            authorAvatar = item.authorAvatar,
            content = item.content.orEmpty(),
            datetime = item.datetime.orEmpty(),
            published = item.published.orEmpty(),
            coords = item.coords,
            eventType = item.type,
            likeOwnerIds = item.likeOwnerIds.orEmpty(),
            likedByMe = item.likedByMe.orFalse(),
            likes = item.likeOwnerIds.orEmpty().size,
            speakerIds = item.speakerIds.orEmpty(),
            participantsIds = item.participantsIds.orEmpty(),
            participatedByMe = item.participatedByMe.orFalse(),
            attachment = item.attachment,
            attachmentMetadata = item.attachment?.let { getAttachmentMetadata(it) },
            link = item.link,
            ownedByMe = item.authorId == auth.authStateFlow.value.id,
            users = item.users,
        )


    fun mapEntityToDto(item: EventEntity): EventDTO =
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
            type = item.eventType,
            likeOwnerIds = item.likeOwnerIds,
            likedByMe = item.likedByMe,
            speakerIds = item.speakerIds,
            participantsIds = item.participantsIds,
            participatedByMe = item.participatedByMe,
            attachment = item.attachment,
            link = item.link,
            users = item.users,
        )

    private suspend fun getAttachmentMetadata(attachment: Attachment): AttachmentMetadata? =
        if (
            attachment.type == AttachmentType.AUDIO ||
            attachment.type == AttachmentType.VIDEO
        ) {
            audioMetadataExtractor.extract(attachment.url)
        } else {
            null
        }
}