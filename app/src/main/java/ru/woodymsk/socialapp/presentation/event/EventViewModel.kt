package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.post.model.EventsItem
import ru.woodymsk.socialapp.domain.event.EventRepository
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _events = MutableLiveData<List<EventsItem>>()
    val events: LiveData<List<EventsItem>>
        get() = _events

    init {
        loadAllEvents()
    }

    private fun loadAllEvents() = viewModelScope.launch {
        _events.value = eventRepository.getAllEventList()
    }
}