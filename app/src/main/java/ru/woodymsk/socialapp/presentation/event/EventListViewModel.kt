package ru.woodymsk.socialapp.presentation.event

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.convertDateFromIsoFormat
import ru.woodymsk.socialapp.domain.event.interactor.EventInteractor
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.BaseViewModel
import ru.woodymsk.socialapp.presentation.common.Screens.authScreen
import ru.woodymsk.socialapp.presentation.common.compose.AudioPlayerManager
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.event.model.EventUiState
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import javax.inject.Inject

// TODO нужна кнопка которая остановит трек на любом экране без необходимости искать его в ленте или воспроизводить другое аудио или видео
// TODO Когда ставишь видео на паузу в фокусе, не должно появляться превью. Превью появляется только если видео ставится на паузу, когда уходит из фокуса

class EventListViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val auth: AppAuth,
    private val router: Router,
    private val videoPlayerManager: VideoPlayerManager,
    private val audioPlayerManager: AudioPlayerManager,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        EventUiState(
            videoPlayerManager = videoPlayerManager,
            audioPlayerManager = audioPlayerManager,
        )
    )
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    init {
        isAuth()
        loadEvents()
        observePlaybackState()
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
            is EventEvents.PlayAudio -> playAudio(event.eventId, event.url)
            is EventEvents.PauseAudio -> pauseAudio(event.eventId)
            is EventEvents.PlayVideo -> playVideo(event.eventId, event.url)
            is EventEvents.PauseVideo -> pauseVideo(event.eventId)
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
        val videoState = videoPlayerManager.state.value
        if (videoState.isPlaying) {
            videoPlayerManager.pause()
            videoPlayerManager.setPlayingState(videoState.playingEventId, false)
        }
    }

    private fun handleError(e: Throwable) =
        updateUIState { it.copy(error = AppError.handleError(e)) }
}