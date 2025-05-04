package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow<EventUiState>(EventUiState.LoadingState)
    val state: StateFlow<EventUiState> = _state.asStateFlow()

    private val _isAuth = MutableStateFlow(false)
    val isAuth: StateFlow<Boolean> = _isAuth.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    fun onEvent(event: EventEvents) {
        when (event) {
            // TODO add event handling that will occur on the event screen
            else -> {}
        }
    }

    init {
        loadAllEvents()
        isAuth()
    }

    fun onBackPressed() = router.exit()

    private fun isAuth() {
        _isAuth.value = auth.authStateFlow.value.id != 0
    }

    private fun loadAllEvents() = viewModelScope.launch(exceptionHandler) {
        _state.value = EventUiState.LoadingState
        val eventList = eventInteractor.getAllEventList()
        _state.value = EventUiState.ShowEvents(eventList)
    }

    private fun handleError(e: Throwable) =
        _state.tryEmit(EventUiState.ErrorEvents(AppError.handleError(e)))
}