package ru.woodymsk.socialapp.presentation.common.compose

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VideoPlayerManager @Inject constructor(
    private val context: Context
) {
    open val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
        }
    }

    private var currentVideoUrl: String? = null
    private var isPlaying: Boolean = false

    fun playVideo(uri: String) {
        if (currentVideoUrl != uri) {
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            currentVideoUrl = uri
        }
        player.play()
        isPlaying = true
    }

    fun pause() {
        player.pause()
        isPlaying = false
    }

    fun pauseIfPlaying(uri: String) {
        if (currentVideoUrl == uri && isPlaying) {
            player.pause()
            isPlaying = false
        }
    }

    fun stop() {
        player.stop()
        currentVideoUrl = null
        isPlaying = false
    }

    fun release() {
        player.release()
        currentVideoUrl = null
        isPlaying = false
    }

    fun isPlaying(uri: String): Boolean {
        return currentVideoUrl == uri && isPlaying
    }
}