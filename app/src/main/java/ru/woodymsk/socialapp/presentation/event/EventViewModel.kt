package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.core_coroutine.util.EventFlow
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.error.handler
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
) : ViewModel() {

    val events = EventFlow<EventsEvent>()

    init {
        loadAllEvents()
    }

    private fun loadAllEvents() = viewModelScope.launch(handler) {
        val eventList = eventInteractor.getAllEventList()
        events.emit(EventsEvent.ShowEvents(eventList))
    }
}