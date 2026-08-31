package ru.woodymsk.socialapp.presentation.event.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.C
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.formatDate
import ru.woodymsk.socialapp.presentation.common.compose.AudioCard
import ru.woodymsk.socialapp.presentation.common.compose.AudioPlayerManager
import ru.woodymsk.socialapp.presentation.common.compose.LikeButton
import ru.woodymsk.socialapp.presentation.common.compose.LoadAvatar
import ru.woodymsk.socialapp.presentation.common.compose.LoadImage
import ru.woodymsk.socialapp.presentation.common.compose.ParticipantButton
import ru.woodymsk.socialapp.presentation.common.compose.PreviewVideoImageWithDurationAndPlayButton
import ru.woodymsk.socialapp.presentation.common.compose.ShareButton
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerWithControls
import ru.woodymsk.socialapp.presentation.common.compose.getLineSymbolCount
import ru.woodymsk.socialapp.presentation.common.compose.getTextLayoutResult
import ru.woodymsk.socialapp.presentation.common.compose.rememberAudioProgress
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme
import ru.woodymsk.socialapp.presentation.theme.robotoFamily
import ru.woodymsk.socialapp.presentation.theme.typography

private const val VISIBLE_ROW_COUNT = 3

//// Ссылка на экран в Figma: https://www.figma.com/design/8z1sV6KIf6Sc1y02TrY2XS/Nmedia?node-id=13-2277&t=5ySWMskd3v90oJSg-1
@Composable
fun EventItem(
    event: Event,
    onEvent: (EventEvents) -> Unit,
    videoPlayerManager: VideoPlayerManager,
    audioPlayerManager: AudioPlayerManager,
    isVideoPlaying: Boolean = false,
    isCurrentAudio: Boolean = false,
    isAudioPlaying: Boolean = false,
    onVideoPlayPause: (Boolean) -> Unit = {},
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = getTextLayoutResult(
        textMeasurer = textMeasurer,
        text = event.content,
        style = typography().bodyMedium,
    )
    val lineCount = textLayoutResult.lineCount
    val lineSymbolCount = getLineSymbolCount(
        text = event.content,
        lineCount = lineCount,
        textLayoutResult = textLayoutResult,
        visibleRowCount = VISIBLE_ROW_COUNT
    )
    val isContentExpanded = remember { mutableStateOf(lineCount <= VISIBLE_ROW_COUNT) }
    val isMenuExpanded = remember { mutableStateOf(false) }
    val isMenuVisibility = remember { MutableTransitionState(event.ownedByMe) }
    val interactionSource = remember { MutableInteractionSource() }
    val displayDescriptionText =
        if (isContentExpanded.value) event.content else event.content.take(lineSymbolCount)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    onEvent(EventEvents.GoToEventDetailScreen(event.id))
                },
        ) {
            // header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                    // publication time
                    Text(
                        text = formatDate(event.published),
                        style = typography().bodyMedium,
                        maxLines = 1,
                    )
                }
                // spacer
                Spacer(modifier = Modifier
                    .weight(1f)
                    .height(48.dp))
                // context menu button
                AnimatedVisibility(visibleState = isMenuVisibility) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_more_vert_24),
                            contentDescription = stringResource(id = R.string.more),
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) { isMenuExpanded.value = true }
                        )
                        DropdownMenu(
                            expanded = isMenuExpanded.value,
                            onDismissRequest = { isMenuExpanded.value = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.edit)) },
                                onClick = {
                                    onEvent(EventEvents.GoToNewEventScreen(event))
                                    isMenuExpanded.value = false
                                },
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.delete)) },
                                onClick = {
                                    onEvent(EventEvents.DeleteEvent(event.id.toString()))
                                    isMenuExpanded.value = false
                                },
                            )
                        }
                    }
                }
            }
            // event attachment image
            if (event.attachment?.type == AttachmentType.IMAGE) {
                LoadImage(url = event.attachment.url)
            }
            // event attachment video
            if (event.attachment?.type == AttachmentType.VIDEO) {
                if (isVideoPlaying) {
                    VideoPlayerWithControls(
                        videoUrl = event.attachment.url,
                        videoPlayerManager = videoPlayerManager,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f),
                        onPlayingChanged = onVideoPlayPause,
                        onError = { onEvent(EventEvents.Error(it)) },
                    )
                } else {
                    PreviewVideoImageWithDurationAndPlayButton(
                        videoUri = event.attachment.url,
                        duration = event.attachmentMetadata?.duration,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) {
                                onVideoPlayPause(true)
                            }
                    )
                }
            }
            // event attachment audio
            if (event.attachment?.type == AttachmentType.AUDIO) {
                val staticDuration = event.attachmentMetadata?.duration ?: "--:--"
                var audioProgress by rememberAudioProgress(
                    player = audioPlayerManager.player,
                    isPlaying = isAudioPlaying,
                    isCurrent = isCurrentAudio,
                    staticDuration = staticDuration,
                )

                AudioCard(
                    title = event.attachmentMetadata?.title ?: stringResource(R.string.unknown_audio_title),
                    artist = event.attachmentMetadata?.artist ?: stringResource(R.string.unknown_audio_artist),
                    duration = audioProgress.displayDuration,
                    isPlaying = isAudioPlaying,
                    onPlayPause = {
                        if (isAudioPlaying) {
                            onEvent(EventEvents.PauseAudio(event.id))
                        } else {
                            onEvent(EventEvents.PlayAudio(event.id, event.attachment.url))
                        }
                    },
                    progress = audioProgress.progress,
                    seekEnabled = isCurrentAudio,
                    onSeek = { fraction ->
                        val player = audioPlayerManager.player
                        val total = player.duration
                        if (total != C.TIME_UNSET && total > 0) {
                            player.seekTo((fraction * total).toLong())
                            // Опрос обновит progress только через ~200мс — без этого слайдер на
                            // мгновение отскакивает к старой позиции, пока не придёт новый тик
                            audioProgress = audioProgress.copy(progress = fraction)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // text content
            Column(modifier = Modifier.padding(16.dp)) {
                event.type?.name?.let {
                    Text(
                        text = it,
                        style = typography().bodyLarge,
                        maxLines = 1,
                    )
                }
                Text(
                    text = formatDate(event.datetime),
                    style = typography().bodyMedium,
                    maxLines = 1,
                )
                if (event.content.isNotEmpty()) {
                    Text(
                        text = buildAnnotatedString {
                            append(displayDescriptionText)
                            withStyle(
                                SpanStyle(
                                    fontSize = 14.sp,
                                    fontFamily = robotoFamily,
                                    fontWeight = FontWeight.W500,
                                    color = colorResource(R.color.purple_typography),
                                )
                            ) {
                                // if isn't Expanded, add button "read more"
                                if (!isContentExpanded.value) {
                                    append(stringResource(R.string.read_next))
                                }
                            }
                        },
                        style = typography().bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                if (lineCount > VISIBLE_ROW_COUNT) {
                                    isContentExpanded.value = !isContentExpanded.value
                                }
                            },
                    )
                }
                // actions bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // like button
                    LikeButton(
                        isLiked = event.likedByMe,
                        modifier = Modifier.padding(start = 12.dp, end = 8.dp),
                        onClick = { onEvent(EventEvents.Like(event.id)) },
                    )
                    // count of likes
                    Box(
                        modifier = Modifier.size(width = 53.dp, height = 40.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = event.likes,
                            modifier = Modifier.padding(end = 4.dp),
                            style = typography().labelLarge,
                        )
                    }
                    // share button
                    ShareButton(
                        iconTint = colorResource(R.color.purple_typography),
                        onClick = {
                            // TODO add share event function
                        },
                    )
                    // spacer
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    )
                    // number of participants counter
                    ParticipantButton(
                        isParticipant = event.participatedByMe,
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            // TODO add function of taking part in an event
                        },
                    )
                    // participants number
                    Box(
                        modifier = Modifier.size(width = 26.dp, height = 40.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = event.participantsNumber,
                            style = typography().labelLarge,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewEventItem() {
    val mockVideoPlayerManager = MockVideoPlayerManager(LocalContext.current)
    val mockAudioPlayerManager = AudioPlayerManager(LocalContext.current)

    SocialAppTheme {
        EventItem(
            event = mockEvent,
            onEvent = {},
            videoPlayerManager = mockVideoPlayerManager,
            audioPlayerManager = mockAudioPlayerManager,
        )
    }
}

private val mockEvent = Event(
    id = 219,
    authorId = 41,
    author = "123",
    authorAvatar = null,
    content = "88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888 88888",
    datetime = "2024-07-06T03:57:00Z",
    published = "2024-07-06T19:57:28.974Z",
    type = EventType.ONLINE,
    likeOwnerIds = listOf(),
    likedByMe = true,
    likes = "988",
    speakerIds = listOf(96),
    participantsIds = listOf(96),
    participatedByMe = false,
    participantsNumber = "10",
    attachment = null,
    ownedByMe = false,
)