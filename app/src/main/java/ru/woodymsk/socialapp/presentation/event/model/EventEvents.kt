package ru.woodymsk.socialapp.presentation.event.model

import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

sealed class EventEvents {
    data class DeleteEvent(val id: String) : EventEvents()
    data class Error(val error: AppError?) : EventEvents()
    data class GoToNewEventScreen(val event: Event? = null) : EventEvents()
    data class GoToEventDetailScreen(val id: Int) : EventEvents()
    data class Like(val id: Int) : EventEvents()
    data class PlayAudio(val eventId: Int, val url: String) : EventEvents()
    data class PauseAudio(val eventId: Int) : EventEvents()
    data class PlayVideo(val eventId: Int, val url: String) : EventEvents()
    data class PauseVideo(val eventId: Int) : EventEvents()
    object GoToLoginScreen : EventEvents()
    object HideAuthDialog : EventEvents()
}