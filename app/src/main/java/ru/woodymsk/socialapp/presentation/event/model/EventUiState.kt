package ru.woodymsk.socialapp.presentation.event.model

import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

sealed class EventUiState {
    data class ShowEvents(val events: List<Event>) : EventUiState()
    data class ErrorEvents(val appError: AppError) : EventUiState()
    data object LoadingState : EventUiState()
}