package ru.woodymsk.socialapp.domain.event

import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.data.model.MediaUpload

interface EventRepository {

    suspend fun getAllEventList(): List<EventDAO>
    suspend fun createEvent(eventDAO: EventDAO, upload: MediaUpload?)
    suspend fun deleteEvent(id: String)
}