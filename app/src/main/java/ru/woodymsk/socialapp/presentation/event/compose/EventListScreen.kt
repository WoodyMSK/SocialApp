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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.formatDate
import ru.woodymsk.socialapp.presentation.common.compose.LoadAvatar
import ru.woodymsk.socialapp.presentation.common.compose.LoadImage
import ru.woodymsk.socialapp.presentation.common.compose.getLineSymbolCount
import ru.woodymsk.socialapp.presentation.common.compose.getTextLayoutResult
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.event.model.EventUiState
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme
import ru.woodymsk.socialapp.presentation.theme.robotoFamily
import ru.woodymsk.socialapp.presentation.theme.typography

private const val VISIBLE_ROW_COUNT = 3

// Ссылка на экран в Figma: https://www.figma.com/design/8z1sV6KIf6Sc1y02TrY2XS/Nmedia?node-id=13-2511&t=MYc1RzcPw7trI81f-1
@Composable
fun EventListScreen(
    state: EventUiState,
    onEvent: (EventEvents) -> Unit,
) {
    val isFabVisibility = remember { MutableTransitionState(state.isAuth) }
    val textMeasurer = rememberTextMeasurer()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        items(items = state.events, key = { item -> item.id }) { event ->

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
                        Box(
                            modifier = Modifier
                                .size(40.dp),
                        ) {
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
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        )
                        // context menu button
                        AnimatedVisibility(visibleState = isMenuVisibility) {
                            Box(
                                modifier = Modifier.size(48.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painterResource(id = R.drawable.ic_more_vert_24),
                                    contentDescription = stringResource(id = R.string.more),
                                    modifier = Modifier
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                        ) {
                                            isMenuExpanded.value = !isMenuExpanded.value
                                        }
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded.value,
                                onDismissRequest = { isMenuExpanded.value = false },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.edit)) },
                                    onClick = {
                                        // TODO add edit event function
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
                    // event attachment image
                    if (event.attachment?.type == AttachmentType.IMAGE) {
                        LoadImage(url = event.attachment.url)
                    }
                    // text content
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                    ) {
                        // event type
                        event.type?.name?.let {
                            Text(
                                text = it,
                                style = typography().bodyLarge,
                                maxLines = 1,
                            )
                        }
                        // event time
                        Text(
                            text = formatDate(event.datetime),
                            style = typography().bodyMedium,
                            maxLines = 1,
                        )
                        // event description
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
                                .padding(vertical = 32.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                ) {
                                    isContentExpanded.value = !isContentExpanded.value
                                },
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
                                        // TODO add like event function
                                    }
                            )
                            // count of likes
                            Box(
                                modifier = Modifier.size(width = 33.dp, height = 40.dp),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Text(
                                    text = if (event.likes != 0) {
                                        event.likes.toString() // TODO add converter function, more than 999 likes can be converted to 1k and so on
                                    } else {
                                        stringResource(R.string.empty_text)
                                    },
                                    modifier = Modifier.padding(end = 4.dp),
                                    style = typography().labelLarge,
                                )
                            }
                            // share button
                            Image(
                                painterResource(id = R.drawable.ic_share_figma_18),
                                contentDescription = stringResource(id = R.string.share_button),
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = interactionSource,
                                    ) {
                                        // TODO add share event function
                                    }
                            )
                            // spacer
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                            )
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
                                    text = if (event.participantsIds.isNotEmpty()) {
                                        event.participantsIds.size.toString()
                                    } else {
                                        stringResource(R.string.empty_text)
                                    },
                                    style = typography().labelLarge,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // add event fab
    AnimatedVisibility(visibleState = isFabVisibility) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 24.dp, bottom = 24.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            FloatingActionButton(
                onClick = {
                    onEvent(EventEvents.GoToNewEventScreen(true))
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(painterResource(id = R.drawable.ic_add_24), stringResource(R.string.add_event))
            }
        }
    }
}

@Preview
@Composable
fun EventScreenPreview() {
    SocialAppTheme {
        EventListScreen(
            state = EventUiState(events = mockEvents),
            onEvent = {},
        )
    }
}

private val mockEvents = listOf(

    Event(
        id = 218,
        authorId = 41,
        author = "123",
        authorAvatar = null,
        content = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\nwwwwwwwwwwwwwwwwww\nwwwwwww\nwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww",
        datetime = "2024-07-06T19:56:00Z",
        published = "2024-07-06T19:56:55.272Z",
        type = EventType.ONLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 55,
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        attachment = Attachment(
            url = "https://ik.imagekit.io/ube3bjrcz/2a47fb22-7803-411b-800e-c2b03c2402a7_ABW9phBTe.jpg",
            type = AttachmentType.IMAGE,
        ),
        ownedByMe = true,
    ),

    Event(
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
        likes = 3,
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        attachment = null,
        ownedByMe = false,
    ),

    Event(
        id = 220,
        authorId = 41,
        author = "123",
        authorAvatar = null,
        content = "ХОЗЯИН И МЫШИ\\nКоль в доме станут воровать,\\nА нет прилики вору,\\nТо берегись клепать,\\nИли наказывать всех сплошь и без разбору:\\nТы вора этим не уймешь\\nИ не исправишь,\\nА только добрых слуг с двора бежать заставишь,\\nИ от меньшой беды в большую попадешь.\\nКупчина выстроил анбары\\nИ в них поклал съестные все товары.\\nА чтоб мышиный род ему не навредил,\\nТак он полицию из кошек учредил.\\nСпокоен от Мышей Купчина;\\nПо кладовым и день и ночь дозор;\\nИ всё бы хорошо, да сделалась причина:\\nВ дозорных появился вор.\\nУ кошек, как у нас (кто этого не знает?),\\nНе без греха в надсмотрщиках бывает.\\nТут, чем бы вора подстеречь\\nИ наказать его, а правых поберечь,\\nХозяин мой велел всех кошек пересечь.\\nУслыша приговор такой замысловатый,\\nИ правый тут, и виноватый\\nСкорей с двора долой.\\nБез кошек стал Купчина мой.\\nА Мыши лишь того и ждали, и хотели:\\nЛишь кошки вон, они — в анбар,\\nИ в две иль три недели\\nПоели весь товар.",
        datetime = "2024-07-06T20:11:00Z",
        published = "2024-07-06T20:11:32.281Z",
        type = EventType.ONLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 999,
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        attachment = null,
        ownedByMe = true,
    ),

    Event(
        id = 217,
        authorId = 41,
        author = "123",
        authorAvatar = null,
        content = "qwe",
        datetime = "2024-07-06T19:42:53.773Z",
        published = "2024-07-06T19:47:20.740Z",
        type = EventType.OFFLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 0,
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        attachment = null,
        ownedByMe = false,
    ),

    Event(
        id = 215,
        authorId = 71,
        author = "kate",
        authorAvatar = null,
        content = "New online",
        datetime = "2024-06-29T00:00:00Z",
        published = "2024-06-23T12:54:38.565Z",
        type = EventType.ONLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 100,
        speakerIds = listOf(87),
        participantsIds = listOf(87),
        participatedByMe = false,
        attachment = null,
        ownedByMe = false,
    ),

    Event(
        id = 210,
        authorId = 85,
        author = "Mark",
        authorAvatar = "https://ik.imagekit.io/ube3bjrcz/b4ba0049-cea6-48ca-9caf-2ae5c185431e_Hfwf7Chmz.jpg",
        content = "Какой-то Повар, грамотей,\\nС поварни побежал своей\\nВ кабак (он набожных был правил\\nИ в этот день по куме тризну правил),\\nА дома стеречи съестное от мышей\\nКота оставил.",
        datetime = "2024-06-21T02:14:33.874Z",
        published = "2024-06-10T09:15:18.163Z",
        type = EventType.ONLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 7,
        speakerIds = listOf(87),
        participantsIds = listOf(87),
        participatedByMe = false,
        attachment = null,
        ownedByMe = false,
    ),

    Event(
        id = 208,
        authorId = 85,
        author = "Mark",
        authorAvatar = "https://ik.imagekit.io/ube3bjrcz/b4ba0049-cea6-48ca-9caf-2ae5c185431e_Hfwf7Chmz.jpg",
        content = "ХОЗЯИН И МЫШИ\\nКоль в доме станут воровать,\\nА нет прилики вору,\\nТо берегись клепать,\\nИли наказывать всех сплошь и без разбору:\\nТы вора этим не уймешь\\nИ не исправишь,\\nА только добрых слуг с двора бежать заставишь,\\nИ от меньшой беды в большую попадешь.\\nКупчина выстроил анбары\\nИ в них поклал съестные все товары.\\nА чтоб мышиный род ему не навредил,\\nТак он полицию из кошек учредил.\\nСпокоен от Мышей Купчина;\\nПо кладовым и день и ночь дозор;\\nИ всё бы хорошо, да сделалась причина:\\nВ дозорных появился вор.\\nУ кошек, как у нас (кто этого не знает?),\\nНе без греха в надсмотрщиках бывает.\\nТут, чем бы вора подстеречь\\nИ наказать его, а правых поберечь,\\nХозяин мой велел всех кошек пересечь.\\nУслыша приговор такой замысловатый,\\nИ правый тут, и виноватый\\nСкорей с двора долой.\\nБез кошек стал Купчина мой.\\nА Мыши лишь того и ждали, и хотели:\\nЛишь кошки вон, они — в анбар,\\nИ в две иль три недели\\nПоели весь товар.",
        datetime = "2024-06-29T02:20:33.874Z",
        published = "2024-06-10T08:43:30.888Z",
        type = EventType.ONLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 34,
        speakerIds = listOf(87),
        participantsIds = listOf(87),
        participatedByMe = false,
        attachment = null,
        ownedByMe = false,
    ),

    Event(
        id = 205,
        authorId = 68,
        author = "evgeny",
        authorAvatar = "https://ik.imagekit.io/ube3bjrcz/1dcd6992-a0da-441c-908a-085c259c64ae_3ppxigjSA.png",
        content = "Content",
        datetime = "2024-06-21T01:13:33.874Z",
        published = "2024-06-09T22:16:47.936Z",
        type = EventType.ONLINE,
        likeOwnerIds = listOf(),
        likedByMe = false,
        likes = 0,
        speakerIds = listOf(
            56,
            59,
            60,
            61
        ),
        participantsIds = listOf(68),
        participatedByMe = false,
        attachment = Attachment(
            url = "https://ik.imagekit.io/ube3bjrcz/7152e3af-322c-4607-a52d-86f906bffd98_XZDy6EmFc.jpg",
            type = AttachmentType.IMAGE,
        ),
        ownedByMe = false,
    ),
)