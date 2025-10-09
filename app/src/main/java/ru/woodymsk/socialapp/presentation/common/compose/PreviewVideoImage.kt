package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.getVideoDuration
import ru.woodymsk.socialapp.presentation.theme.typography

@Composable
fun PreviewVideoImage(
    videoUri: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(context)
            .data(videoUri)
            .crossfade(true)
            // Извлечение кадра на 1 секунде видео для превью
            .videoFrameMillis(1000)
            .build(),
        imageLoader = imageLoader,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = colorResource(id = R.color.purple_typography),
                )
            }
        },
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(R.string.preview_video),
    )
}

@Composable
fun PreviewVideoImageWithDuration(
    videoUri: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var duration by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(videoUri) {
        duration = withContext(Dispatchers.IO) {
            getVideoDuration(context, videoUri)
        }
    }

    Box(modifier = modifier) {
        PreviewVideoImage(videoUri, modifier = Modifier.fillMaxSize())

        // Отображение длительности видео в правом нижнем углу
        duration?.let {
            Text(
                text = it,
                style = typography().labelSmall.copy(
                    color = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}