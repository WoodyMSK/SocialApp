package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.event.model.EventUiState
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val auth: AppAuth,
    private val router: Router,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    fun onEvent(event: EventEvents) {
        when (event) {
            is EventEvents.Loading -> updateUIState {
                it.copy(isLoading = event.isLoading)
            }
            is EventEvents.Error -> updateUIState {
                it.copy(error = event.error)
            }
            is EventEvents.DeleteEvent -> deleteEvent(event.id)
            is EventEvents.GoToNewEventScreen -> updateUIState {
                it.copy(isGoToNewEventScreen = event.isGoToNewEventScreen)
            }
        }
    }

    init {
        loadAllEvents()
        isAuth()
    }

    fun onBackPressed() = router.exit()

    private fun isAuth() {
        updateUIState { it.copy(isAuth = auth.authStateFlow.value.id != 0) }
    }

    private fun loadAllEvents() = viewModelScope.launch(exceptionHandler) {
        updateUIState { it.copy(isLoading = true) }
        val eventList = eventInteractor.getAllEventList()
        updateUIState { it.copy(events = eventList) }
        updateUIState { it.copy(isLoading = false) }
    }

    private fun deleteEvent(id: String) {
        viewModelScope.launch(exceptionHandler) {
            eventInteractor.deleteEvent(id)
        }
    }

    private fun updateUIState(updater: (EventUiState) -> EventUiState) {
        _uiState.update(updater)
    }

    private fun handleError(e: Throwable) =
        updateUIState { it.copy(error = AppError.handleError(e)) }
}