package ru.woodymsk.socialapp.domain.event

import ru.woodymsk.socialapp.data.event.model.EventDAO

interface EventRepository {

    suspend fun getAllEventList(): List<EventDAO>

}