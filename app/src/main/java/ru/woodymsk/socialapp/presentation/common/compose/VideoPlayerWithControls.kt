package ru.woodymsk.socialapp.presentation.common.compose

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import ru.woodymsk.socialapp.error.AppError

private const val TAG = "VideoPlayerWithControls"

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerWithControls(
    videoUrl: String,
    videoPlayerManager: VideoPlayerManager,
    modifier: Modifier = Modifier,
    onPlayingChanged: (Boolean) -> Unit = {},
    onError: (AppError) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnError by rememberUpdatedState(onError)
    val listener = remember {
        object : Player.Listener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                onPlayingChanged(playWhenReady)
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e(
                    TAG,
                    "player error for url=$videoUrl code=${error.errorCodeName} " +
                        "message=${error.message}",
                    error,
                )
                currentOnError(AppError.handleError(error))
            }
        }
    }

    // videoPlayerManager.player — общий Singleton ExoPlayer, который на момент привязки к
    // свежесозданному PlayerView мог уже быть подготовлен/играть с другого экрана. Сам по себе
    // он не перерисовывает кадр на новую Surface — получается чёрный/залипший кадр, пока плеер
    // не отвязать и не привязать заново
    var hasForcedSurfaceRedraw by remember(videoUrl) { mutableStateOf(false) }

    DisposableEffect(videoPlayerManager.player) {
        videoPlayerManager.player.addListener(listener)

        onDispose {
            videoPlayerManager.player.removeListener(listener)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    videoPlayerManager.player.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    // We do not resume automatically - management via the UI
                }
                Lifecycle.Event.ON_DESTROY -> {
                    // We are not releasing here because the video player is a singleton
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Setup media source when changing the URL
    LaunchedEffect(videoUrl) {
        videoPlayerManager.playVideo(videoUrl)
    }

    // Android View for displaying videos with controls
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = videoPlayerManager.player
                useController = true
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            }
        },
        modifier = modifier,
        update = { playerView ->
            if (!hasForcedSurfaceRedraw) {
                hasForcedSurfaceRedraw = true
                val player = videoPlayerManager.player
                playerView.player = null
                playerView.player = player
                player.seekTo(player.currentPosition)
                playerView.invalidate()
            }
        },
    )
}