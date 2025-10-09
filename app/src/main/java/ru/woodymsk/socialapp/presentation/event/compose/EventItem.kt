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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.formatDate
import ru.woodymsk.socialapp.presentation.common.compose.LoadAvatar
import ru.woodymsk.socialapp.presentation.common.compose.LoadImage
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerWithControls
import ru.woodymsk.socialapp.presentation.common.compose.getLineSymbolCount
import ru.woodymsk.socialapp.presentation.common.compose.getTextLayoutResult
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
            Modifier.background(MaterialTheme.colorScheme.surface),
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
                VideoPlayerWithControls(
                    videoUrl = event.attachment.url,
                    videoPlayerManager = videoPlayerManager,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
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
                                append(stringResource(R.string.read_next)) //TODO bug. При нажатии на любое сообщение, вне зависимости от количества строк, оно будет свёрнуто
                            }
                        }
                    },
                    style = typography().bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) { isContentExpanded.value = !isContentExpanded.value },
                )
                // actions bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // like button
                    Image(
                        painterResource(
                            id = if (event.likedByMe) {
                                R.drawable.ic_like_filled_18
                            } else {
                                R.drawable.ic_like_outlined_18
                            }
                        ),
                        contentDescription = stringResource(R.string.like_button),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                onEvent(EventEvents.Like(event.id))
                            }
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
                    Image(
                        painterResource(id = R.drawable.ic_share_figma_18),
                        contentDescription = stringResource(id = R.string.share_button),
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                // TODO add share event function
                            }
                    )
                    // spacer
                    Spacer(modifier = Modifier
                        .weight(1f)
                        .height(40.dp))
                    // number of participants counter
                    Image(
                        painterResource(id = R.drawable.ic_people_outline_22),
                        contentDescription = stringResource(R.string.participants_icon),
                        modifier = Modifier.padding(end = 8.dp),
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

    SocialAppTheme {
        EventItem(
            event = mockEvent,
            onEvent = {},
            videoPlayerManager = mockVideoPlayerManager,
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
    likes = "98888888",
    speakerIds = listOf(96),
    participantsIds = listOf(96),
    participatedByMe = false,
    participantsNumber = "10",
    attachment = null,
    ownedByMe = false,
)