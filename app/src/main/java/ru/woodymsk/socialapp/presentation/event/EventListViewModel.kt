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
import ru.woodymsk.socialapp.presentation.common.Screens.authScreen
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.event.model.EventUiState
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val auth: AppAuth,
    private val router: Router,
    private val videoPlayerManager: VideoPlayerManager,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(EventUiState(videoPlayerManager = videoPlayerManager))
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
            is EventEvents.Like -> like(event.id)
            is EventEvents.GoToLoginScreen -> goToLoginScreen()
            is EventEvents.GoToEventDetailScreen -> goToEventDetailsScreen(event.id)
            is EventEvents.HideAuthDialog -> hideAuthDialog()
        }
    }

    fun onBackPressed() = router.exit()

    private fun goToLoginScreen() = router.replaceScreen(authScreen())

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

    private fun goToEventDetailsScreen(id: Int) =
        navigateTo(Screen.EventDetailsScreen(id))

    private fun isAuth() {
        updateUIState { it.copy(isAuth = auth.authStateFlow.value.id != 0) }
    }

    private fun deleteEvent(id: String) {
        viewModelScope.launch(exceptionHandler) {
            eventInteractor.deleteEvent(id)
        }
    }

    private fun like(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            if (uiState.value.isAuth) {
                eventInteractor.like(id)
            } else {
                showAuthDialog()
            }
        }
    }

    private fun showAuthDialog() {
        updateUIState { it.copy(showAuthDialog = true) }
    }

    private fun hideAuthDialog() {
        updateUIState { it.copy(showAuthDialog = false) }
    }

    private fun updateUIState(updater: (EventUiState) -> EventUiState) {
        _uiState.update(updater)
    }

    override fun onCleared() {
        super.onCleared()
        videoPlayerManager.release()
    }

    private fun handleError(e: Throwable) = updateUIState { it.copy(error = AppError.handleError(e)) }
}