package ru.woodymsk.socialapp.presentation.event_details.model

import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager

data class EventDetailsUIState(
    val event: Event = Event(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val videoPlayerManager: VideoPlayerManager,
)