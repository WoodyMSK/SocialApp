package ru.woodymsk.socialapp.presentation.event.model

import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

sealed class EventEvents {
    data class Loading(val isLoading: Boolean) : EventEvents()
    data class DeleteEvent(val id: String) : EventEvents()
    data class Error(val error: AppError?) : EventEvents()
    data class GoToNewEventScreen(val event: Event? = null) : EventEvents()
}