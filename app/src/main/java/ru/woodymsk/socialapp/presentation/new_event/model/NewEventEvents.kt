package ru.woodymsk.socialapp.presentation.new_event.model

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

sealed class NewEventEvents {
    data class ContentUpdated(val newContent: String) : NewEventEvents()
    data class DataTimeUpdated(val newDataTime: String) : NewEventEvents()
    data class TypeUpdated(val newEventType: EventType?) : NewEventEvents()
    data class AttachmentUpdated(val newAttachment: Attachment?) : NewEventEvents()
    data class DateTimeBottomSheetState(val isShowDateTimeBottomSheet: Boolean) : NewEventEvents()
    data class Loading(val isLoading: Boolean) : NewEventEvents()
    data class Error(val error: AppError?) : NewEventEvents()
    data class EditEvent(val editEvent: Event?) : NewEventEvents()
    data object GoToBackScreen : NewEventEvents()
    data object CreateEvent : NewEventEvents()
    data object DismissDataInvalid : NewEventEvents()
}