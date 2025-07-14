package ru.woodymsk.socialapp.presentation.event.model

import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

data class EventUiState(
    val events: List<Event> = emptyList(),
    val isAuth: Boolean = false,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)