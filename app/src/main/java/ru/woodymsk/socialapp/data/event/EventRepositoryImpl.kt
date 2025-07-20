package ru.woodymsk.socialapp.data.event

import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.api.EventService
import ru.woodymsk.socialapp.data.event.db.EventDao
import ru.woodymsk.socialapp.data.event.mapper.EventMapper
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.domain.throwAppError
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventService: EventService,
    private val eventMapper: EventMapper,
    private val postRepository: PostRepository,
    private val eventDao: EventDao,
) : EventRepository {

    override fun getEventFlow() : Flow<List<EventEntity>> = eventDao.getEventFlow()

    override suspend fun refreshEventList() = withContextIO(handler) {
        val response = eventService.getAllEventList()
        if (!response.isSuccessful) response.body().throwAppError(response)
        val events = eventMapper.mapListDtoToListEntity(response.body().orEmpty())
        eventDao.removeAllEvents()
        eventDao.insertEventList(events)
    }

    override suspend fun createEvent(eventEntity: EventEntity, upload: MediaUpload?): Unit =
        withContextIO(handler) {
            val response = if (upload == null) {
                eventService.createEvent(eventMapper.mapEntityToDto(eventEntity))
            } else {
                val media = postRepository.uploadMedia(upload)

                eventService.createEvent(
                    eventMapper.mapEntityToDto(
                        eventEntity.copy(
                            attachment = Attachment(
                                media.url,
                                AttachmentType.IMAGE
                            )
                        )
                    )
                )
            }
            if (!response.isSuccessful) response.body().throwAppError(response)
            response.body()?.let { eventDao.insertEvent(eventMapper.mapDtoToEntity(it)) }
        }

    override suspend fun deleteEvent(id: String) =
        withContextIO(handler) {
            val response = eventService.removeEventById(id)
            if (!response.isSuccessful) response.body().throwAppError(response)
            eventDao.removeEventById(id.toInt())
        }
}