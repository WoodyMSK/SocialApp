package ru.woodymsk.socialapp.presentation.event.model

import ru.woodymsk.socialapp.domain.event.model.Event

sealed class EventsEvent {
    data class ShowEvents(val events: List<Event>) : EventsEvent()
}
