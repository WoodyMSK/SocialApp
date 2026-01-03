package ru.woodymsk.socialapp.presentation.new_event

import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.isValidDate
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.BaseViewModel
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventUiState
import ru.woodymsk.socialapp.presentation.post.model.PictureModel
import javax.inject.Inject

class NewEventViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()

    private val eventPicture = mutableStateOf(PictureModel())
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    fun onEvent(event: NewEventEvents) {
        when (event) {
            is NewEventEvents.ContentUpdated -> updateUIState {
                it.copy(event = it.event.copy(content = event.newContent))
            }
            is NewEventEvents.DataTimeUpdated -> updateUIState {
                it.copy(
                    event = it.event.copy(datetime = event.newDataTime),
                )
            }
            is NewEventEvents.TypeUpdated -> updateUIState {
                it.copy(event = it.event.copy(type = event.newEventType))
            }
            is NewEventEvents.AttachmentUpdated -> updateUIState {
                eventPicture.value = PictureModel(event.newAttachment?.url?.toUri())
                it.copy(event = it.event.copy(attachment = event.newAttachment))
            }
            is NewEventEvents.CoordsUpdated -> updateUIState {
                it.copy(event = it.event.copy(coords = event.newCoords))
            }
            is NewEventEvents.CreateEvent -> {
                createDataChecked()
                if (uiState.value.eventDataInvalid == null) {
                    createEvent()
                }
            }
            is NewEventEvents.DateTimeBottomSheetState -> updateUIState {
                it.copy(isShowDateTimeBottomSheet = event.isShowDateTimeBottomSheet)
            }
            is NewEventEvents.Loading -> updateUIState {
                it.copy(isLoading = event.isLoading)
            }
            is NewEventEvents.Error -> updateUIState {
                it.copy(error = event.error)
            }
            is NewEventEvents.EditEvent -> updateUIState {
                it.copy(event = event.editEvent ?: Event())
            }
            is NewEventEvents.GoToBackScreen -> onBackPressed()
            is NewEventEvents.GoToMapScreen -> navigateTo(Screen.MapScreen)
            is NewEventEvents.DismissDataInvalid -> updateUIState {
                it.copy(eventDataInvalid = null)
            }
        }
    }

    fun onBackPressed() = navigateTo(Screen.PopBackStack)

    private fun updateUIState(updater: (NewEventUiState) -> NewEventUiState) {
        _uiState.update(updater)
    }

    private fun createEvent() =
        viewModelScope.launch(exceptionHandler) {
            updateUIState { it.copy(isLoading = true) }
            _uiState.value.let {
                when (eventPicture.value) {
                    PictureModel() -> eventInteractor.createEvent(it.event, null)
                    else -> eventPicture.value.uri?.let { uri ->
                        eventInteractor.createEvent(it.event, MediaUpload(uri.toFile()))
                    }
                }
            }
            _uiState.value = NewEventUiState()
            eventPicture.value = PictureModel()
            updateUIState { it.copy(isLoading = false) }
            onBackPressed()
        }

    private fun createDataChecked() {
        when {
            _uiState.value.event.content.isEmpty() -> {
                updateUIState { it.copy(eventDataInvalid = R.string.message_is_blank) }
                updateUIState { it.copy(isShowDateTimeBottomSheet = true) }
            }

            isValidDate(_uiState.value.event.datetime).not() -> {
                updateUIState { it.copy(eventDataInvalid = R.string.date_in_past) }
                updateUIState { it.copy(isShowDateTimeBottomSheet = true) }
            }

            else -> updateUIState { it.copy(eventDataInvalid = null) }
        }
    }

    private fun handleError(e: Throwable) =
        updateUIState { it.copy(error = AppError.handleError(e)) }
}