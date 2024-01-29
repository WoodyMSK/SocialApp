package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.core_coroutine.util.EventFlow
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.domain.navigation.subnavigation.LocalCiceroneHolder
import ru.woodymsk.socialapp.error.handler
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.presentation.common.TabTag.EVENT_SCREEN
import ru.woodymsk.socialapp.presentation.event.model.EventsEvent
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val ciceroneHolder: LocalCiceroneHolder,
) : ViewModel(), RouterProvider {

    val events = EventFlow<EventsEvent>()

    override val router: Router
        get() = ciceroneHolder.getCicerone(EVENT_SCREEN).router

    init {
        loadAllEvents()
    }

    fun onBackPressed() = router.exit()

    private fun loadAllEvents() = viewModelScope.launch(handler) {
        val eventList = eventInteractor.getAllEventList()
        events.emit(EventsEvent.ShowEvents(eventList))
    }
}