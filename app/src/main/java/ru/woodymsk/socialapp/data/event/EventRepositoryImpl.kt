package ru.woodymsk.socialapp.data.event

import ru.woodymsk.socialapp.data.api.EventService
import ru.woodymsk.socialapp.data.event.mapper.EventMapper
import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventService: EventService,
    private val eventMapper: EventMapper,
) : EventRepository {

    override suspend fun getAllEventList(): List<EventDAO> = withContextIO(handler) {
        val response = eventService.getAllEventList()
        eventMapper.mapToDao(response.body().orEmpty())
    }
}