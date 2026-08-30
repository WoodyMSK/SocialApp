package ru.woodymsk.socialapp.presentation.common.compose

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AudioPlayerManager @Inject constructor(
    private val context: Context
) {
    open val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    private val _state = MutableStateFlow(MediaPlaybackState())
    val state: StateFlow<MediaPlaybackState> = _state.asStateFlow()
    private var currentAudioUrl: String? = null

    fun playAudio(uri: String) {
        if (currentAudioUrl != uri) {
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            currentAudioUrl = uri
        }
        player.play()
    }

    fun pauseAudio() {
        player.pause()
    }

    fun setPlayingState(eventId: Int?, isPlaying: Boolean) {
        _state.value = MediaPlaybackState(eventId, isPlaying)
    }
}