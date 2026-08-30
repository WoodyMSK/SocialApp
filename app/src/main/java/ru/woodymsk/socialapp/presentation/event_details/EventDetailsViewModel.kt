package ru.woodymsk.socialapp.presentation.event_details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.BaseViewModel
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsEvents
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsUIState
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import javax.inject.Inject

class EventDetailsViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val videoPlayerManager: VideoPlayerManager,
) : BaseViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    private val _uiState =
        MutableStateFlow(EventDetailsUIState(videoPlayerManager = videoPlayerManager))
    val uiState: StateFlow<EventDetailsUIState> = _uiState.asStateFlow()

    fun onEvent(event: EventDetailsEvents) {
        when (event) {
            is EventDetailsEvents.LoadEvent -> getEventFromDB(event.id)
            is EventDetailsEvents.GoToBackScreen -> onBackPressed()
            is EventDetailsEvents.Loading -> showProgressBar(event.isLoading)
            is EventDetailsEvents.Error -> handleError(event.error)
        }
    }

    private fun onBackPressed() = navigateTo(Screen.PopBackStack)

    private fun getEventFromDB(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            showProgressBar(true)
            val event = eventInteractor.getEventFromDB(id)
            updateUIState { it.copy(event = event) }
            showProgressBar(false)
        }
    }

    private fun showProgressBar(visible: Boolean) =
        updateUIState { it.copy(isLoading = visible) }

    private fun updateUIState(updater: (EventDetailsUIState) -> EventDetailsUIState) {
        _uiState.update(updater)
    }

    private fun handleError(error: Throwable?) {
        updateUIState { it.copy(error = error?.let { AppError.handleError(it) }) }
    }
}