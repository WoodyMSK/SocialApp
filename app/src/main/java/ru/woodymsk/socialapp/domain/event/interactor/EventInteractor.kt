package ru.woodymsk.socialapp.domain.event.interactor

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
}