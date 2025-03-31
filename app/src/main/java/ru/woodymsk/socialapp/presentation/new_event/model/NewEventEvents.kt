package ru.woodymsk.socialapp.presentation.new_event.model

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.EventType

sealed class NewEventEvents {
    data class ContentUpdated(val newContent: String) : NewEventEvents()
    data class DataTimeUpdated(val newDataTime: String) : NewEventEvents()
    data class TypeUpdated(val newEventType: EventType?) : NewEventEvents()
    data class AttachmentUpdated(val newAttachment: Attachment?) : NewEventEvents()
}