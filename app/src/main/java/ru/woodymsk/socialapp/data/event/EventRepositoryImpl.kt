package ru.woodymsk.socialapp.data.event

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.api.EventService
import ru.woodymsk.socialapp.data.event.db.EventDao
import ru.woodymsk.socialapp.data.event.db.EventDatabase
import ru.woodymsk.socialapp.data.event.db.EventKeyDao
import ru.woodymsk.socialapp.data.event.mapper.EventMapper
import ru.woodymsk.socialapp.data.event.model.EventEntity
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
    private val eventKeyDao: EventKeyDao,
    private val eventDatabase: EventDatabase,
) : EventRepository {

    companion object {
        const val PAGE_SIZE = 10
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedEventList(): Flow<PagingData<EventEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = 3 * PAGE_SIZE,
                initialLoadSize = 2 * PAGE_SIZE,
            ),
            pagingSourceFactory = eventDao::getPagingSource,
            remoteMediator = EventRemoteMediator(
                eventService = eventService,
                eventDao = eventDao,
                eventKeyDao = eventKeyDao,
                eventDatabase = eventDatabase,
                eventMapper = eventMapper,
            )
        ).flow

    override suspend fun createEvent(eventEntity: EventEntity, upload: MediaUpload?): Unit =
        withContextIO(handler) {
            val response = if (upload == null) {
                eventService.createEvent(eventMapper.mapEntityToDto(eventEntity))
            } else {
                val media = postRepository.uploadMedia(upload)

                eventService.createEvent(
                    eventMapper.mapEntityToDto(
                        eventEntity.copy(
                            attachment = eventEntity.attachment?.copy(url = media.url)
                        )
                    )
                )
            }
            if (!response.isSuccessful) response.body().throwAppError(response)
            response.body()?.let { eventDao.insertEvent(eventMapper.mapDtoToEntity(it)) }
        }

    override suspend fun insertEventToDB(event: EventEntity) =
        withContextIO(handler) {
            eventDao.insertEvent(event)
        }

    override suspend fun getEventFromDB(id: Int): EventEntity =
        withContextIO(handler) {
            eventDao.getEventById(id)
        }

    override suspend fun deleteEvent(id: String) =
        withContextIO(handler) {
            val response = eventService.removeEventById(id)
            if (!response.isSuccessful) response.body().throwAppError(response)
            eventDao.removeEventById(id.toInt())
        }

    override suspend fun removeAllDbEvents() =
        withContextIO(handler) {
            eventDao.removeAllEvents()
        }

    override suspend fun like(event: EventEntity) {
        withContextIO(handler) {
            eventDao.insertEvent(event)
            val response = eventService.like(event.id)
            val responseEvent = eventMapper.mapDtoToEntity(response.body().throwAppError(response))
            if (responseEvent != event) {
                eventDao.insertEvent(responseEvent)
            }
        }
    }

    override suspend fun deleteLike(event: EventEntity) {
        withContextIO(handler) {
            eventDao.insertEvent(event)
            val response = eventService.deleteLike(event.id)
            val responseEvent = eventMapper.mapDtoToEntity(response.body().throwAppError(response))
            if (responseEvent != event) {
                eventDao.insertEvent(responseEvent)
            }
        }
    }
}