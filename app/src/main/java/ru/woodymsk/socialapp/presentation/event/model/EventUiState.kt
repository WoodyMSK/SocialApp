package ru.woodymsk.socialapp.presentation.event.model

import ru.woodymsk.socialapp.domain.event.model.Event

sealed class EventUiState {
    data class ShowEvents(val events: List<Event>) : EventUiState()
    data object LoadingState : EventUiState()
}