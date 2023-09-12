package ru.woodymsk.socialapp.presentation.event

import ru.woodymsk.socialapp.domain.event.model.Event

sealed class EventsEvent {
    data class ShowEvents(val events: List<Event>) : EventsEvent()
}
