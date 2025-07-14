package ru.woodymsk.socialapp.presentation.new_event.model

import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

data class NewEventUiState(
    val event: Event = Event(),
    val eventDataInvalid: Int? = null,
    val isShowDateTimeBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)