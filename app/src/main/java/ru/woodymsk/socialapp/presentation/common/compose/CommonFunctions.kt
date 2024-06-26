package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import ru.woodymsk.socialapp.R

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
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        loading = { CircularProgressIndicator() },
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(R.string.load_image_by_coil),
    )
}