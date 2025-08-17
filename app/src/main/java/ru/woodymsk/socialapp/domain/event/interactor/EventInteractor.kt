package ru.woodymsk.socialapp.domain.event.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.event.mapper.EventMapper
import ru.woodymsk.socialapp.domain.event.model.Event
import javax.inject.Inject

class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {

    fun getPagedEventList(): Flow<PagingData<Event>> =
        eventMapper.mapEventFromEntity(eventRepository.getPagedEventList())

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