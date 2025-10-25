package ru.woodymsk.socialapp.presentation.event_details.model

import ru.woodymsk.socialapp.error.AppError

sealed class EventDetailsEvents {
    data class LoadEvent(val id: Int) : EventDetailsEvents()
    data class Loading(val isLoading: Boolean) : EventDetailsEvents()
    data class Error(val error: AppError?) : EventDetailsEvents()
    data object GoToBackScreen : EventDetailsEvents()
}