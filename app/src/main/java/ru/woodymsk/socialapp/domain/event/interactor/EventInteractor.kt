package ru.woodymsk.socialapp.domain.event.interactor

import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.event.mapper.EventMapper
import ru.woodymsk.socialapp.domain.event.model.Event
import javax.inject.Inject

class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {

    suspend fun getAllEventList(): List<Event> =
        eventMapper.mapEventFromDao(eventRepository.getAllEventList())

    suspend fun createEvent(
        event: Event,
        upload: MediaUpload?
    ) {
        eventRepository.createEvent(
            eventDAO = eventMapper.mapSingleEventToDAO(event),
            upload = upload,
        )
    }

    suspend fun deleteEvent(id: String) = eventRepository.deleteEvent(id)
}