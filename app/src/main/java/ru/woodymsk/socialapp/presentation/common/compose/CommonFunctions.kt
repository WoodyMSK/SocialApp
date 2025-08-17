package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.theme.typography

@Composable
fun LoadAvatar(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.ic_profile_24),
        error = painterResource(id = R.drawable.ic_error_24),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .fillMaxSize(),
        contentDescription = stringResource(R.string.load_avatar_by_coil),
    )
}

@Composable
fun LoadImage(url: String) {
    SubcomposeAsyncImage(
        modifier = Modifier.fillMaxWidth(),
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