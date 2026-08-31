package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import ru.woodymsk.socialapp.domain.formatVideoDuration
import kotlin.time.Duration.Companion.milliseconds

data class AudioProgress(val displayDuration: String, val progress: Float)

// Опрашивает позицию проигрывания раз в 200мс, пока трек играет; если трек на паузе,
// но остаётся "текущим" — синхронизируется один раз, не запуская цикл опроса
@Composable
fun rememberAudioProgress(
    player: ExoPlayer,
    isPlaying: Boolean,
    isCurrent: Boolean,
    staticDuration: String,
): MutableState<AudioProgress> {
    val state = remember { mutableStateOf(AudioProgress(staticDuration, 0f)) }

    LaunchedEffect(isPlaying, isCurrent) {
        fun syncFromPlayer() {
            val total = player.duration
            if (total != C.TIME_UNSET && total > 0) {
                val remaining = (total - player.currentPosition).coerceAtLeast(0)
                state.value = AudioProgress(
                    displayDuration = formatVideoDuration(remaining),
                    progress = (player.currentPosition.toFloat() / total).coerceIn(0f, 1f),
                )
            }
        }
        when {
            isPlaying -> {
                while (isActive) {
                    syncFromPlayer()
                    delay(200.milliseconds)
                }
            }
            isCurrent -> syncFromPlayer()
            else -> state.value = AudioProgress(staticDuration, 0f)
        }
    }

    return state
}
