package ru.woodymsk.socialapp.domain.event

import ru.woodymsk.socialapp.domain.post.model.EventsItem

interface EventRepository {

    suspend fun getAllEventList(): List<EventsItem>

}