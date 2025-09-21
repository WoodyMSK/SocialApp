package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerWithControls(
    videoUrl: String,
    videoPlayerManager: VideoPlayerManager,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    videoPlayerManager.player.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    videoPlayerManager.player.play()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    // Не освобождаем здесь, так как это синглтон
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
            }
        },
        modifier = modifier
    )
}