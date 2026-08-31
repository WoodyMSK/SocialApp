package ru.woodymsk.socialapp.presentation.event_details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.BaseViewModel
import ru.woodymsk.socialapp.presentation.common.compose.AudioPlayerManager
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsEvents
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsUIState
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import javax.inject.Inject

class EventDetailsViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val videoPlayerManager: VideoPlayerManager,
    private val audioPlayerManager: AudioPlayerManager,
) : BaseViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    private val _uiState =
        MutableStateFlow(
            EventDetailsUIState(
                videoPlayerManager = videoPlayerManager,
                audioPlayerManager = audioPlayerManager,
            )
        )
    val uiState: StateFlow<EventDetailsUIState> = _uiState.asStateFlow()

    init {
        observePlaybackState()
    }

    fun onEvent(event: EventDetailsEvents) {
        when (event) {
            is EventDetailsEvents.LoadEvent -> getEventFromDB(event.id)
            is EventDetailsEvents.GoToBackScreen -> onBackPressed()
            is EventDetailsEvents.Loading -> showProgressBar(event.isLoading)
            is EventDetailsEvents.PlayAudio -> playAudio(event.eventId, event.url)
            is EventDetailsEvents.PauseAudio -> pauseAudio(event.eventId)
            is EventDetailsEvents.PlayVideo -> playVideo(event.eventId, event.url)
            is EventDetailsEvents.PauseVideo -> pauseVideo(event.eventId)
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

    // Состояние audioPlayerManager/videoPlayerManager (Singleton) в uiState, чтобы UI читал всё из единого источника истины
    private fun observePlaybackState() {
        viewModelScope.launch {
            combine(audioPlayerManager.state, videoPlayerManager.state) { audio, video ->
                audio to video
            }.collect { (audio, video) ->
                updateUIState {
                    it.copy(
                        playingAudioEventId = audio.playingEventId,
                        isAudioPlaying = audio.isPlaying,
                        playingVideoEventId = video.playingEventId,
                        isVideoPlaying = video.isPlaying,
                    )
                }
            }
        }
    }

    private fun playAudio(eventId: Int, url: String) {
        val audioState = audioPlayerManager.state.value
        // Если уже играет это аудио, ставим на паузу
        if (audioState.playingEventId == eventId && audioState.isPlaying) {
            pauseAudio(eventId)
            return
        }

        // Останавливаем видео, если оно играет
        val videoState = videoPlayerManager.state.value
        if (videoState.isPlaying) {
            videoPlayerManager.pause()
            videoPlayerManager.setPlayingState(videoState.playingEventId, false)
        }

        audioPlayerManager.playAudio(url)
        audioPlayerManager.setPlayingState(eventId, true)
    }

    private fun pauseAudio(eventId: Int) {
        if (audioPlayerManager.state.value.playingEventId == eventId) {
            audioPlayerManager.pauseAudio()
            audioPlayerManager.setPlayingState(eventId, false)
        }
    }

    private fun playVideo(eventId: Int, url: String) {
        val videoState = videoPlayerManager.state.value
        // Если уже играет это видео, ставим на паузу
        if (videoState.playingEventId == eventId && videoState.isPlaying) {
            pauseVideo(eventId)
            return
        }

        // Останавливаем текущее видео если играет другое
        if (videoState.isPlaying) {
            videoPlayerManager.pause()
        }

        // Останавливаем аудио, если оно играет
        val audioState = audioPlayerManager.state.value
        if (audioState.isPlaying) {
            audioPlayerManager.pauseAudio()
            audioPlayerManager.setPlayingState(audioState.playingEventId, false)
        }

        videoPlayerManager.playVideo(url)
        videoPlayerManager.setPlayingState(eventId, true)
    }

    private fun pauseVideo(eventId: Int) {
        if (videoPlayerManager.state.value.playingEventId == eventId) {
            videoPlayerManager.pause()
            videoPlayerManager.setPlayingState(eventId, false)
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