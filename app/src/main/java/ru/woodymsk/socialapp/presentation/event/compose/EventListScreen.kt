package ru.woodymsk.socialapp.presentation.event.compose

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.flowOf
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.compose.AlertDialog
import ru.woodymsk.socialapp.presentation.common.compose.AppendLoadError
import ru.woodymsk.socialapp.presentation.common.compose.LoadingIndicator
import ru.woodymsk.socialapp.presentation.common.compose.RefreshLoadError
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.event.model.EventUiState
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme
import android.content.Context
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


// Ссылка на экран в Figma: https://www.figma.com/design/8z1sV6KIf6Sc1y02TrY2XS/Nmedia?node-id=13-2511&t=1S5gJ3zZWiBBGUYm-1
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventListScreen(
    state: EventUiState,
    onEvent: (EventEvents) -> Unit,
) {
    val lazyPagingItems = state.pagingDataFlow.collectAsLazyPagingItems()
    val isFabVisibility = remember { MutableTransitionState(state.isAuth) }
    var isLoadingIndicatorVisibility by remember { mutableStateOf(false) }
    var isRefreshLoadErrorVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoadingIndicatorVisibility,
        onRefresh = {
            lazyPagingItems.refresh()
        }
    )
    val listState = rememberLazyListState()
    val mostVisibleVideoEvent = remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(listState, lazyPagingItems) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                // Find most visible video event
                val videoEvents = visibleItems.mapNotNull { item ->
                    try {
                        // check that the index is valid and the element exists
                        if (item.index >= 0 && item.index < lazyPagingItems.itemCount) {
                            lazyPagingItems[item.index]?.takeIf { event ->
                                event.attachment?.type == AttachmentType.VIDEO
                            }?.let { event ->
                                Pair(event, item)
                            }
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }

                if (videoEvents.isNotEmpty()) {
                    // Select most visible video (with the maximum viewing area)
                    val mostVisible = videoEvents.maxByOrNull { (event, item) ->
                        val visibleTop = maxOf(item.offset, listState.layoutInfo.viewportStartOffset)
                        val visibleBottom = minOf(item.offset + item.size, listState.layoutInfo.viewportEndOffset)
                        visibleBottom - visibleTop
                    }?.first

                    if (mostVisible != mostVisibleVideoEvent.value) {
                        mostVisibleVideoEvent.value = mostVisible

                        // Play new video
                        mostVisible?.attachment?.url?.let { url ->
                            state.videoPlayerManager.playVideo(url)
                        }
                    }
                } else {
                    // if there are no visible videos, then pause video player
                    if (mostVisibleVideoEvent.value != null) {
                        state.videoPlayerManager.pause()
                        mostVisibleVideoEvent.value = null
                    }
                }
            }
    }

    // Initial Upload processing
    LaunchedEffect(lazyPagingItems.loadState) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                isLoadingIndicatorVisibility = true
                isRefreshLoadErrorVisibility = false
            }

            is LoadState.Error -> {
                val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                onEvent(EventEvents.Error(AppError.handleError(error)))
                isLoadingIndicatorVisibility = false
                isRefreshLoadErrorVisibility = lazyPagingItems.itemCount == 0
            }

            is LoadState.NotLoading -> {
                isLoadingIndicatorVisibility = false
                isRefreshLoadErrorVisibility = false
            }
        }
    }

    // show Toast message in case of an error
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Toast.makeText(
                context,
                error.code,
                Toast.LENGTH_LONG
            ).show()
            // Сбрасываем ошибку после показа
            onEvent(EventEvents.Error(null))
        }
    }

    if (state.showAuthDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(EventEvents.HideAuthDialog) },
            onConfirmation = { onEvent(EventEvents.GoToLoginScreen) },
            dialogTitle = stringResource(id = R.string.require_authorization),
            dialogText = stringResource(id = R.string.execute_login),
            titleTextSize = 20.sp,
            buttonTextSize = 16.sp,
            icon = Icons.Default.Info,
        )
    }

    Box(Modifier.fillMaxSize()) {
        if (isRefreshLoadErrorVisibility && isLoadingIndicatorVisibility.not()) {
            // show the error screen in case of an error
            RefreshLoadError(
                onRetry = { lazyPagingItems.refresh() } // TODO исправить мерцание экрана при обновлении
            )
        } else if (lazyPagingItems.itemCount > 0 || lazyPagingItems.loadState.refresh is LoadState.Loading) {
            // show event list
            Column(modifier = Modifier.pullRefresh(pullRefreshState)) {
                LazyColumn(
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { event -> event.id },
                        contentType = lazyPagingItems.itemContentType { "Events" },
                    ) { index ->
                        val event = lazyPagingItems[index]
                        if (event != null) {
                            EventItem(
                                event = event,
                                onEvent = onEvent,
                                videoPlayerManager = state.videoPlayerManager,
                            )
                        }
                    }
                    // processing the loading states of new events
                    lazyPagingItems.loadState.apply {
                        when {
                            // load indicator
                            append is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isLoadingIndicatorVisibility.not()) {
                                            LoadingIndicator()
                                        }
                                    }
                                }
                            }
                            // show the error at the end of the list
                            append is LoadState.Error -> {
                                item {
                                    if (isLoadingIndicatorVisibility.not()) {
                                        AppendLoadError(
                                            onRetry = { lazyPagingItems.retry() } // TODO исправить мерцание экрана при обновлении
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // pullRefresh load indicator
            PullRefreshIndicator(
                refreshing = isLoadingIndicatorVisibility,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
    // add event fab
    AnimatedVisibility(
        visibleState = isFabVisibility
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 24.dp, bottom = 24.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            FloatingActionButton(
                onClick = {
                    onEvent(EventEvents.GoToNewEventScreen())
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_add_24),
                    stringResource(R.string.add_event)
                )
            }
        }
    }
}

@Preview
@Composable
fun EventScreenPreview() {
    val mockVideoPlayerManager = MockVideoPlayerManager(LocalContext.current)
    val mockState = EventUiState(
        isAuth = true,
        pagingDataFlow = flowOf(PagingData.from(mockEvents)),
        showAuthDialog = false,
        videoPlayerManager = mockVideoPlayerManager,
    )

    SocialAppTheme {
        EventListScreen(
            state = mockState,
            onEvent = {},
        )
    }
}

class MockVideoPlayerManager(context: Context) : VideoPlayerManager(context) {
    override val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
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
        likes = "55",
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        participantsNumber = "345",
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
        likes = "3",
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        participantsNumber = "35",
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
        likes = "999",
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        participantsNumber = "45",
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
        likes = "0",
        speakerIds = listOf(96),
        participantsIds = listOf(96),
        participatedByMe = false,
        participantsNumber = "3453",
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
        likes = "100",
        speakerIds = listOf(87),
        participantsIds = listOf(87),
        participatedByMe = false,
        participantsNumber = "3435345",
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
        likes = "7",
        speakerIds = listOf(87),
        participantsIds = listOf(87),
        participatedByMe = false,
        participantsNumber = "3",
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
        likes = "34",
        speakerIds = listOf(87),
        participantsIds = listOf(87),
        participatedByMe = false,
        participantsNumber = "344565",
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
        likes = "0",
        speakerIds = listOf(
            56,
            59,
            60,
            61
        ),
        participantsIds = listOf(68),
        participatedByMe = false,
        participantsNumber = "34",
        attachment = Attachment(
            url = "https://ik.imagekit.io/ube3bjrcz/7152e3af-322c-4607-a52d-86f906bffd98_XZDy6EmFc.jpg",
            type = AttachmentType.IMAGE,
        ),
        ownedByMe = false,
    ),
)