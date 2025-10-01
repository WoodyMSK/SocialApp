package ru.woodymsk.socialapp.domain.event.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.data.model.UserPreview
import ru.woodymsk.socialapp.data.profile.ProfilePreferences
import ru.woodymsk.socialapp.domain.convertDateToIsoFormat
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.event.mapper.EventMapper
import ru.woodymsk.socialapp.domain.event.model.Event
import javax.inject.Inject

class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val profilePrefs: ProfilePreferences,
) {

    fun getPagedEventList(): Flow<PagingData<Event>> =
        eventMapper.mapEventFromEntity(eventRepository.getPagedEventList())

    suspend fun createEvent(
        event: Event,
        upload: MediaUpload?
    ) {
        eventRepository.createEvent(
            eventEntity = eventMapper.mapSingleEventToEntity(
                item = event.copy(
                    datetime = convertDateToIsoFormat(event.datetime)
                )
            ),
            upload = upload,
        )
    }

    suspend fun deleteEvent(id: String) = eventRepository.deleteEvent(id)
    suspend fun like(id: Int) {
        val event = eventMapper.mapSingleEventEntityToEvent(eventRepository.getEventFromDB(id))
        val updatedEvent = if (event.likedByMe) {
            event.copy(
                likedByMe = false,
                likeOwnerIds = event.likeOwnerIds.filterNot { it == profilePrefs.profileStateFlow.value.id },
                users = event.users.filterKeys { it != profilePrefs.profileStateFlow.value.id }
            )
        } else {
            event.copy(
                likedByMe = true,
                likeOwnerIds = event.likeOwnerIds + profilePrefs.profileStateFlow.value.id,
                users = event.users.plus(
                    Pair(
                        first = profilePrefs.profileStateFlow.value.id,
                        second = UserPreview(
                            name = profilePrefs.profileStateFlow.value.name,
                            avatar = profilePrefs.profileStateFlow.value.avatar,
                        )
                    )
                )
            )
        }

        runCatching {
            if (event.likedByMe) {
                eventRepository.deleteLike(eventMapper.mapSingleEventToEntity(updatedEvent))
            } else {
                eventRepository.like(eventMapper.mapSingleEventToEntity(updatedEvent))
            }
        }.onFailure { e ->
            eventRepository.insertEventToDB(eventMapper.mapSingleEventToEntity(event))
            throw e
        }
    }
}