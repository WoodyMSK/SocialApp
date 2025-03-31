package ru.woodymsk.socialapp.presentation.new_event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents
import javax.inject.Inject

class NewEventViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(Event())
        private set

    fun onEvent(event: NewEventEvents) {
        this.state = when (event) {
            is NewEventEvents.ContentUpdated -> state.copy(content = event.newContent)
            is NewEventEvents.DataTimeUpdated -> state.copy(datetime = event.newDataTime)

            is NewEventEvents.TypeUpdated -> state.copy(type = event.newEventType)
            is NewEventEvents.AttachmentUpdated -> state.copy(attachment = event.newAttachment)
        }
    }

}