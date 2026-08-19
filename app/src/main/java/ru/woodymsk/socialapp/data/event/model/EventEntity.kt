package ru.woodymsk.socialapp.data.event.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.data.model.UserPreview
import ru.woodymsk.socialapp.domain.common.model.AttachmentMetadata

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorJob: String?,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coords?,
    val eventType: EventType?,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val likes: Int,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    @Embedded
    val attachment: Attachment?,
    @Embedded(prefix = "metadata_")
    val attachmentMetadata: AttachmentMetadata?,
    val link: String?,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>,
)