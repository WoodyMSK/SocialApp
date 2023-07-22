package ru.woodymsk.socialapp.domain.event_screen

import ru.woodymsk.socialapp.data.model.EventsItem

interface EventRepository {
    val events: List<EventsItem>

    suspend fun getAllEventList()
}