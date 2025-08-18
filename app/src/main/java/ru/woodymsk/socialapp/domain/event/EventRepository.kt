package ru.woodymsk.socialapp.domain.event

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.data.model.MediaUpload

interface EventRepository {
    fun getPagedEventList(): Flow<PagingData<EventEntity>>
    suspend fun createEvent(eventEntity: EventEntity, upload: MediaUpload?)
    suspend fun deleteEvent(id: String)
    suspend fun removeAllDbEvents()
}