package ru.woodymsk.socialapp.domain.event.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.event.mapper.EventMapper
import ru.woodymsk.socialapp.domain.event.model.Event
import javax.inject.Inject

class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {

    fun getEventFlow(): Flow<List<Event>> =
        eventRepository.getEventFlow().map { events -> eventMapper.mapEventFromDao(events) }

    suspend fun refreshEventList() = eventRepository.refreshEventList()

    suspend fun createEvent(
        event: Event,
        upload: MediaUpload?
    ) {
        eventRepository.createEvent(
            eventEntity = eventMapper.mapSingleEventToDAO(event),
            upload = upload,
        )
    }

    suspend fun deleteEvent(id: String) = eventRepository.deleteEvent(id)
}