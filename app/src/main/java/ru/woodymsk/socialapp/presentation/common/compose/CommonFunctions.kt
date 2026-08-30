package ru.woodymsk.socialapp.presentation.common.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.theme.typography

@Composable
fun LoadAvatar(
    url: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.error_avatar),
        error = painterResource(id = R.drawable.error_avatar),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .fillMaxSize(),
        contentDescription = stringResource(R.string.load_avatar_by_coil),
    )
}

@Composable
fun LoadImage(
    url: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .size(40.dp),
                color = colorResource(id = R.color.purple_typography),
            )
        },
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(R.string.load_image_by_coil),
    )
}

@Composable
fun getScreenWidthPx(): Int =
    with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.roundToPx() }

@Composable
fun getTextLayoutResult(
    textMeasurer: TextMeasurer,
    text: String,
    style: TextStyle,
): TextLayoutResult = textMeasurer.measure(
    text,
    style = style,
    constraints = Constraints(maxWidth = getScreenWidthPx()),
)

@Composable
fun getLineSymbolCount(
    text: String,
    lineCount: Int,
    textLayoutResult: TextLayoutResult,
    visibleRowCount: Int,
): Int {
    var lineText: String
    var lineSymbolCount = 0

    if (lineCount < visibleRowCount) return 0
    // Вычисляем количество символов на каждой строке и суммируем их. Если количестов строк менее VISIBLE_ROW_COUNT, то возвращаем 0, чтобы не делать лищние вычисления
    for (i in 0 until visibleRowCount) {
        val lineStart = textLayoutResult.getLineStart(i)
        val lineEnd = textLayoutResult.getLineEnd(i)
        lineText = text.substring(lineStart, lineEnd)
        lineSymbolCount += lineText.length
    }

    return lineSymbolCount
}

@Composable
fun AppendLoadError(onRetry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(R.string.download_error),
            style = typography().bodyMedium,
            color = Color.Gray,
        )
        Text(
            modifier = Modifier.clickable { onRetry() },
            text = stringResource(R.string.retry),
            style = typography().labelLarge,
        )
    }
}

@Composable
fun RefreshLoadError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(id = R.drawable.ic_wifi_4_bar_off_96),
            contentDescription = stringResource(R.string.participants_icon),
            modifier = Modifier,
        )
        Text(
            modifier = Modifier,
            text = stringResource(R.string.download_error_check_connection),
            style = typography().bodyMedium,
            color = Color.Gray,
        )
        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .clickable { onRetry() },
            text = stringResource(R.string.try_again),
            style = typography().labelLarge,
        )
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun RemoveAttachmentButton(
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.7f),
                shape = CircleShape
            )
            .clickable {
                onRemove()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close_24),
            contentDescription = stringResource(R.string.remove_button_label),
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun PlayVideoButton(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_play_24),
        contentDescription = stringResource(R.string.play_video_button),
        modifier = modifier
            .size(60.dp)
            .background(
                color = Color.Black.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .padding(12.dp),
        tint = Color.White
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Icon(
        painter = painterResource(id = if (isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_24),
        contentDescription = stringResource(
            if (isPlaying) R.string.pause_audio else R.string.play_audio
        ),
        modifier = modifier
            .size(60.dp)
            .background(
                color = colorResource(id = R.color.purple_typography),
                shape = CircleShape,
            )
            .padding(12.dp)
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
            ) {
                onClick()
            },
        tint = Color.White,
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ShowMoreButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(35.dp)
            .clip(CircleShape)
            .background(colorResource(id = R.color.purple_typography)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_add_24),
            contentDescription = stringResource(R.string.show_more_button),
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                ) {
                    onClick()
                },
            colorFilter = ColorFilter.tint(Color(0xFFFEF7FF)),
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back_24),
            contentDescription = stringResource(R.string.back_button),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                ) {
                    onClick()
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_check_24),
            contentDescription = stringResource(R.string.save_button),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                ) {
                    onClick()
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ShareButton(
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: Dp = 18.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_share_figma_18),
            contentDescription = stringResource(R.string.share_button),
            modifier = Modifier
                .size(iconSize)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                ) {
                    onClick()
                },
            colorFilter = ColorFilter.tint(iconTint),
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun LikeButton(
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painterResource(
                id = if (isLiked) {
                    R.drawable.ic_like_filled_18
                } else {
                    R.drawable.ic_like_outlined_18
                }
            ),
            contentDescription = stringResource(R.string.like_button),
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
            ) {
                onClick()
            },
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ParticipantButton(
    isParticipant: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painterResource(
                id = if (isParticipant) {
                    R.drawable.ic_people_filled_22
                } else {
                    R.drawable.ic_people_outline_22
                }
            ),
            contentDescription = stringResource(R.string.participants_icon),
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
            ) {
                onClick()
            },

        )
    }
}

@Composable
fun AlertDialogButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_typography)),
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            style = typography().labelLarge,
            color = Color.White,
            text = text,
        )
    }
}

@Composable
fun CategoryTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
fun CategoryValue(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
            .clickable { onClick() }
            .padding(bottom = 4.dp),
        color = MaterialTheme.colorScheme.primary,
    )
}