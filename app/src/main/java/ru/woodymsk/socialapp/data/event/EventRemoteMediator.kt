package ru.woodymsk.socialapp.data.event

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult.Error
import androidx.paging.RemoteMediator.MediatorResult.Success
import androidx.room.withTransaction
import ru.woodymsk.socialapp.data.api.EventService
import ru.woodymsk.socialapp.data.event.db.EventDao
import ru.woodymsk.socialapp.data.event.db.EventDatabase
import ru.woodymsk.socialapp.data.event.db.EventKeyDao
import ru.woodymsk.socialapp.data.event.mapper.EventMapper
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.data.event.model.EventKeyEntity
import ru.woodymsk.socialapp.domain.orZero
import ru.woodymsk.socialapp.domain.throwAppError

@ExperimentalPagingApi
class EventRemoteMediator(
    private val eventService: EventService,
    private val eventDao: EventDao,
    private val eventKeyDao: EventKeyDao,
    private val eventDatabase: EventDatabase,
    private val eventMapper: EventMapper,
) : RemoteMediator<Int, EventEntity>() {

    override suspend fun initialize(): InitializeAction {
        return if (eventDao.isEmpty()) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        try {
            val pageSize = state.config.pageSize
            val response = when (loadType) {
                REFRESH -> {
                    // При обновлении очищаем БД
                    eventDao.removeAllEvents()
                    eventKeyDao.removeAllEventKeys()
                    eventService.getLatest(pageSize)
                }
                PREPEND -> {
                    return Success(true)
                }
                APPEND -> {
                    // Получаем последний элемент из состояния пагинации
                    val id = eventKeyDao.minKey() ?: return Success(false)
                    eventService.getBeforeEvent(id.toString(), pageSize)
                }
            }

            val eventList = response.body().throwAppError(response)

            if (eventList.isEmpty()) {
                return Success(endOfPaginationReached = true)
            }

            eventDatabase.withTransaction {
                when (loadType) {
                    REFRESH -> {
                        eventKeyDao.insertEventKeys(
                            listOf(
                                EventKeyEntity(
                                    type = EventKeyEntity.Type.PREPEND,
                                    id = eventList.first().id.orZero(),
                                ),
                                EventKeyEntity(
                                    type = EventKeyEntity.Type.APPEND,
                                    id = eventList.last().id.orZero(),
                                ),
                            )
                        )
                    }
                    PREPEND -> {
                        eventKeyDao.insertEventKey(
                            EventKeyEntity(
                                type = EventKeyEntity.Type.PREPEND,
                                id = eventList.first().id.orZero(),
                            )
                        )
                    }
                    APPEND -> {
                        eventKeyDao.insertEventKey(
                            EventKeyEntity(
                                type = EventKeyEntity.Type.APPEND,
                                id = eventList.last().id.orZero(),
                            )
                        )
                    }
                }

                eventDao.insertEventList(eventMapper.mapListDtoToListEntity(eventList))
            }

            return Success(eventList.isEmpty())
        } catch (e: Exception) {
            return Error(e)
        }
    }
}