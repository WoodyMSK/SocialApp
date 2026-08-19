package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme

@Composable
fun AudioCard(
    title: String,
    artist: String,
    duration: String,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    seekEnabled: Boolean = false,
    onSeek: (Float) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Левая часть: кнопка воспроизведения и информация о треке
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                // Кнопка воспроизведения/паузы
                PlayPauseButton(
                    isPlaying = isPlaying,
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { onPlayPause() }
                )

                // Информация о треке
                Column(
                    modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = Typography().bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = artist,
                        style = Typography().bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            // Правая часть: длительность трека
            Text(
                text = duration,
                style = Typography().bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        var isDragging by remember { mutableStateOf(false) }
        var dragProgress by remember { mutableFloatStateOf(0f) }

        // Слайдер перемотки аудио
        Slider(
            value = if (isDragging) dragProgress else progress,
            onValueChange = {
                dragProgress = it
                isDragging = true
            },
            onValueChangeFinished = {
                isDragging = false
                onSeek(dragProgress)
            },
            enabled = seekEnabled,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.purple_typography),
                activeTrackColor = colorResource(id = R.color.purple_typography),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        HorizontalDivider()
    }

}

@Preview
@Composable
fun PreviewAudioCard() {
    SocialAppTheme {
        AudioCard(
            title = "Song Title",
            artist = "Artist Name",
            duration = "3:45",
            isPlaying = false,
            onPlayPause = {}
        )
    }
}