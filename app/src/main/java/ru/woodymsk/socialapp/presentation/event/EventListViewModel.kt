package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.convertDateFromIsoFormat
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.BaseViewModel
import ru.woodymsk.socialapp.presentation.event.model.EventUiState
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val auth: AppAuth,
    private val router: Router,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    init {
        isAuth()
        loadEvents()
    }

    fun onEvent(event: EventEvents) {
        when (event) {
            is EventEvents.Error -> updateUIState {
                it.copy(error = event.error)
            }
            is EventEvents.DeleteEvent -> deleteEvent(event.id)
            is EventEvents.GoToNewEventScreen -> goToNewEventScreen(
                event.event?.run {
                    copy(datetime = convertDateFromIsoFormat(datetime))
                }
            )
        }
    }

    fun onBackPressed() = router.exit()

    private fun loadEvents() {
        _uiState.update {
            it.copy(
                pagingDataFlow = eventInteractor.getPagedEventList()
                    .cachedIn(viewModelScope)
            )
        }
    }

    private fun goToNewEventScreen(event: Event?) =
        navigateTo(Screen.NewEventScreen(event))

    private fun isAuth() {
        updateUIState { it.copy(isAuth = auth.authStateFlow.value.id != 0) }
    }

    private fun deleteEvent(id: String) {
        viewModelScope.launch(exceptionHandler) {
            eventInteractor.deleteEvent(id)
        }
    }

    private fun updateUIState(updater: (EventUiState) -> EventUiState) {
        _uiState.update(updater)
    }

    private fun handleError(e: Throwable) = updateUIState { it.copy(error = AppError.handleError(e)) }
}