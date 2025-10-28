package ru.woodymsk.socialapp.presentation.event_details.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.data.model.UserPreview
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.formatDate
import ru.woodymsk.socialapp.presentation.common.compose.ShowMoreButton
import ru.woodymsk.socialapp.presentation.common.compose.BackButton
import ru.woodymsk.socialapp.presentation.common.compose.LikeButton
import ru.woodymsk.socialapp.presentation.common.compose.LoadAvatar
import ru.woodymsk.socialapp.presentation.common.compose.LoadImage
import ru.woodymsk.socialapp.presentation.common.compose.ParticipantButton
import ru.woodymsk.socialapp.presentation.common.compose.PreviewVideoImageWithDurationAndPlayButton
import ru.woodymsk.socialapp.presentation.common.compose.ShareButton
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerWithControls
import ru.woodymsk.socialapp.presentation.common.compose.YandexMap
import ru.woodymsk.socialapp.presentation.event.compose.MockVideoPlayerManager
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsEvents
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsUIState
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme
import ru.woodymsk.socialapp.presentation.theme.typography

private const val VISIBLE_SPEAKERS_AVATAR_COUNT = 8
private const val VISIBLE_LIKERS_AVATAR_COUNT = 5
private const val VISIBLE_PARTICIPANTS_AVATAR_COUNT = 5


//// Ссылка на экран в Figma: https://www.figma.com/design/8z1sV6KIf6Sc1y02TrY2XS/Nmedia?node-id=82-3906&m=dev&t=CEBpr1LZUJYlwL1O-1
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    state: EventDetailsUIState,
    onEvent: (EventDetailsEvents) -> Unit,
) {
    val event = state.event
    val isVideoPlaying = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        // top screen bar
        topBar = {
            TopAppBar(
                // screen name
                title = {
                    Text(
                        style = typography().titleLarge,
                        text = stringResource(id = R.string.event_title)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceVariant),
                // back button
                navigationIcon = {
                    BackButton(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        onClick = {
                            onEvent(EventDetailsEvents.GoToBackScreen)
                        }
                    )
                },
                // share event button
                actions = {
                    ShareButton(
                        iconSize = 24.dp,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        onClick = {
                            // TODO add share event function
                        },
                    )
                },
            )
        },
        content = { paddingValues ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            ) {
                LazyColumn(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                ) {
                    item {
                        // header
                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 12.dp, bottom = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // author avatar image
                            Box(modifier = Modifier.size(40.dp)) {
                                LoadAvatar(url = event.authorAvatar.orEmpty())
                            }
                            Column(
                                modifier = Modifier
                                    .size(height = 48.dp, width = 236.dp)
                                    .padding(start = 16.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ) {
                                // author name
                                Text(
                                    text = event.author,
                                    style = typography().titleMedium,
                                    maxLines = 1,
                                )
                                // author job
                                event.authorJob?.let {
                                    Text(
                                        text = it,
                                        style = typography().bodyMedium,
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
                        // event attachment image
                        if (event.attachment?.type == AttachmentType.IMAGE) {
                            LoadImage(
                                url = event.attachment.url,
                                modifier = Modifier.padding(vertical = 8.dp),
                            )
                        }
                        // event attachment video
                        if (event.attachment?.type == AttachmentType.VIDEO) {
                            if (isVideoPlaying.value) {
                                VideoPlayerWithControls(
                                    videoUrl = event.attachment.url,
                                    videoPlayerManager = state.videoPlayerManager,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 9f)
                                        .padding(vertical = 8.dp),
                                )
                            } else {
                                PreviewVideoImageWithDurationAndPlayButton(
                                    videoUri = event.attachment.url,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 9f)
                                        .padding(vertical = 8.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                        ) {
                                            isVideoPlaying.value = true
                                        }
                                )
                            }
                        }
                        // text content
                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 6.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                        ) {
                            event.type?.name?.let {
                                if (event.type == EventType.ONLINE) {
                                    Text(
                                        text = stringResource(id = R.string.event_format_online),
                                        style = typography().titleMedium,
                                    )
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.event_format_offline),
                                        style = typography().titleMedium,
                                    )
                                }

                            }
                            Text(
                                text = formatDate(event.datetime),
                                modifier = Modifier.padding(top = 8.dp),
                                style = typography().bodyMedium,
                            )
                            if (event.content.isNotEmpty()) {
                                Text(
                                    text = event.content,
                                    style = typography().bodyMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                )
                            }
                            // Speakers
                            Text(
                                text = stringResource(R.string.speakers),
                                modifier = Modifier.padding(top = 16.dp),
                                style = typography().titleMedium,
                            )
                            LazyRow(
                                Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy((-8).dp),
                            ) {
                                if (event.speakerIds.isEmpty()) {
                                    item {
                                        Text(
                                            text = stringResource(R.string.no_speakers),
                                            style = typography().bodyMedium.copy(
                                                fontStyle = FontStyle.Italic
                                            ),
                                            maxLines = 1,
                                        )
                                    }
                                }
                                items(event.speakerIds.take(VISIBLE_SPEAKERS_AVATAR_COUNT)) { ids ->
                                    val likerAvatarUrl = event.users[ids]?.avatar.orEmpty()
                                    Box(modifier = Modifier.size(40.dp)) {
                                        LoadAvatar(
                                            url = likerAvatarUrl,
                                            modifier = Modifier
                                                .border(2.dp, Color.White, CircleShape)
                                                .padding(2.dp),
                                        )
                                    }
                                }
                                if (event.speakerIds.isNotEmpty() &&
                                    event.speakerIds.size > VISIBLE_SPEAKERS_AVATAR_COUNT
                                ) {
                                    item {
                                        ShowMoreButton(
                                            modifier = Modifier
                                                .border(2.dp, Color.White, CircleShape)
                                                .padding(2.dp),
                                            onClick = {
                                                // TODO add show full likers list function
                                            },
                                        )
                                    }
                                }
                            }
                            // Likers
                            Text(
                                text = stringResource(R.string.likers),
                                modifier = Modifier.padding(top = 16.dp),
                                style = typography().titleMedium,
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // like button
                                LikeButton(
                                    isLiked = event.likedByMe,
                                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                                    onClick = {
                                        // TODO add like event function
                                    },
                                )
                                // count of likes
                                Box(
                                    modifier = Modifier.size(width = 49.dp, height = 40.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    Text(
                                        text = event.likes,
                                        modifier = Modifier.padding(end = 4.dp),
                                        style = typography().labelLarge,
                                    )
                                }
                                // likers avatars
                                LazyRow(
                                    Modifier,
                                    horizontalArrangement = Arrangement.spacedBy((-8).dp),
                                ) {
                                    items(event.likeOwnerIds.take(VISIBLE_LIKERS_AVATAR_COUNT)) { ids ->
                                        val likerAvatarUrl = event.users[ids]?.avatar.orEmpty()
                                        Box(modifier = Modifier.size(40.dp)) {
                                            LoadAvatar(
                                                url = likerAvatarUrl,
                                                modifier = Modifier
                                                    .border(2.dp, Color.White, CircleShape)
                                                    .padding(2.dp),
                                            )
                                        }
                                    }
                                    if (event.likeOwnerIds.isNotEmpty() &&
                                        event.likeOwnerIds.size > VISIBLE_LIKERS_AVATAR_COUNT
                                    ) {
                                        item {
                                            ShowMoreButton(
                                                modifier = Modifier
                                                    .border(2.dp, Color.White, CircleShape)
                                                    .padding(2.dp),
                                                onClick = {
                                                    // TODO add show full likers list function
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                            // participants
                            Text(
                                text = stringResource(R.string.participants),
                                modifier = Modifier.padding(top = 16.dp),
                                style = typography().titleMedium,
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // number of participants counter
                                ParticipantButton(
                                    isParticipant = event.participatedByMe,
                                    modifier = Modifier.padding(start = 12.dp, end = 8.dp),
                                    onClick = {
                                        // TODO add function of taking part in an event
                                    },
                                )
                                // participants number
                                Box(
                                    modifier = Modifier.size(width = 49.dp, height = 40.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    Text(
                                        text = event.participantsNumber,
                                        style = typography().labelLarge,
                                    )
                                }
                                // participants avatars
                                LazyRow(
                                    Modifier,
                                    horizontalArrangement = Arrangement.spacedBy((-8).dp),
                                ) {
                                    items(
                                        event.participantsIds.take(
                                            VISIBLE_PARTICIPANTS_AVATAR_COUNT
                                        )
                                    ) { ids ->
                                        val likerAvatarUrl = event.users[ids]?.avatar.orEmpty()
                                        Box(modifier = Modifier.size(40.dp)) {
                                            LoadAvatar(
                                                url = likerAvatarUrl,
                                                modifier = Modifier
                                                    .border(2.dp, Color.White, CircleShape)
                                                    .padding(2.dp),
                                            )
                                        }
                                    }
                                    if (event.participantsIds.isNotEmpty() &&
                                        event.participantsIds.size > VISIBLE_PARTICIPANTS_AVATAR_COUNT
                                    ) {
                                        item {
                                            ShowMoreButton(
                                                modifier = Modifier
                                                    .border(2.dp, Color.White, CircleShape)
                                                    .padding(2.dp),
                                                onClick = {
                                                    // TODO add show full participants list function
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                            // map
                            if (event.coords != null) {
                                YandexMap(
                                    initialCameraPosition = CameraPosition(
                                        Point(event.coords.lat, event.coords.long),
                                        15.0f, // zoom
                                        0.0f,  // azimuth
                                        0.0f   // tilt
                                    ),
                                    points = listOf(Point(event.coords.lat, event.coords.long)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp)
                                        .padding(top = 16.dp),
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun PreviewEventDetailsScreen() {
    val mockVideoPlayerManager = MockVideoPlayerManager(LocalContext.current)
    val mockUIState = EventDetailsUIState(
        event = mockEvent,
        videoPlayerManager = mockVideoPlayerManager
    )

    SocialAppTheme {
        EventDetailsScreen(
            state = mockUIState,
            onEvent = {},
        )
    }
}

private val mockEvent = Event(
    id = 219,
    authorId = 41,
    author = "123",
    authorJob = "Roga i Kopita",
    authorAvatar = null,
    content = "88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888",
    datetime = "2024-07-06T03:57:00Z",
    published = "2024-07-06T19:57:28.974Z",
    coords = Coords(lat = 55.751244, long = 37.618423),
    type = EventType.ONLINE,
    likeOwnerIds = listOf(172, 191, 192, 193, 194, 195, 196),
    likedByMe = true,
    likes = "988",
//    speakerIds = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
    speakerIds = listOf(),
    participantsIds = listOf(96, 97, 98, 99, 100, 102, 103),
    participatedByMe = false,
    participantsNumber = "10",
    attachment = null,
    ownedByMe = false,
    users = mapOf(
        172 to UserPreview(
            name = "Evgeny",
            avatar = "https://ik.imagekit.io/ube3bjrcz/88136015-98d2-4aa6-aea5-49bb1ffeff1b_EpXTQ3jDr.jpg",
        ),
        191 to UserPreview(
            name = "Bogdan",
            avatar = null,
        ),
        192 to UserPreview(
            name = "Evgeny",
            avatar = "https://ik.imagekit.io/ube3bjrcz/88136015-98d2-4aa6-aea5-49bb1ffeff1b_EpXTQ3jDr.jpg",
        ),
        193 to UserPreview(
            name = "Evgeny",
            avatar = "https://ik.imagekit.io/ube3bjrcz/88136015-98d2-4aa6-aea5-49bb1ffeff1b_EpXTQ3jDr.jpg",
        ),
        194 to UserPreview(
            name = "Evgeny",
            avatar = "https://ik.imagekit.io/ube3bjrcz/88136015-98d2-4aa6-aea5-49bb1ffeff1b_EpXTQ3jDr.jpg",
        ),
        195 to UserPreview(
            name = "Evgeny",
            avatar = "https://ik.imagekit.io/ube3bjrcz/88136015-98d2-4aa6-aea5-49bb1ffeff1b_EpXTQ3jDr.jpg",
        ),
        1 to UserPreview(
            name = "Nikita Obrekht",
            avatar = "https://ik.imagekit.io/ube3bjrcz/0575e2eb-db62-460c-b800-d9b5a2ec34d8_BwSe4b7g_.jpg",
        ),
        2 to UserPreview(
            name = "test",
            avatar = null,
        ),
        3 to UserPreview(
            name = "Test 2",
            avatar = null,
        ),
        4 to UserPreview(
            name = "Nikita Obrekht",
            avatar = "https://ik.imagekit.io/ube3bjrcz/0575e2eb-db62-460c-b800-d9b5a2ec34d8_BwSe4b7g_.jpg",
        ),
        5 to UserPreview(
            name = "Nikita Obrekht",
            avatar = "https://ik.imagekit.io/ube3bjrcz/0575e2eb-db62-460c-b800-d9b5a2ec34d8_BwSe4b7g_.jpg",
        ),
    )
)