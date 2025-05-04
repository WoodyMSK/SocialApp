package ru.woodymsk.socialapp.data.event

import ru.woodymsk.socialapp.data.api.EventService
import ru.woodymsk.socialapp.data.event.mapper.EventMapper
import ru.woodymsk.socialapp.data.event.model.EventDAO
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventService: EventService,
    private val eventMapper: EventMapper,
    private val postRepository: PostRepository,
) : EventRepository {

    override suspend fun getAllEventList(): List<EventDAO> = withContextIO(handler) {
        val response = eventService.getAllEventList()
        eventMapper.mapToDao(response.body().orEmpty())
    }

    override suspend fun createEvent(eventDAO: EventDAO, upload: MediaUpload?): Unit =
        withContextIO(handler) {
            if (upload == null) {
                eventService.createEvent(eventMapper.mapToDto(eventDAO))
            } else {
                val media = postRepository.uploadMedia(upload)

                eventService.createEvent(
                    eventMapper.mapToDto(
                        eventDAO.copy(
                            attachment = Attachment(
                                media.url,
                                AttachmentType.IMAGE
                            )
                        )
                    )
                )
            }
        }
}