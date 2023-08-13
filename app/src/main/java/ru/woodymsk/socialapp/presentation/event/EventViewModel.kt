package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.handler
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
) : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>>
        get() = _events

    init {
        loadAllEvents()
    }

    private fun loadAllEvents() = viewModelScope.launch(handler) {
        _events.value = eventInteractor.getAllEventList()
    }
}