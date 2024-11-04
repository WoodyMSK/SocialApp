package ru.woodymsk.socialapp.presentation.profile.compose

import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.domain.parseAndFormatDate
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.domain.profile.model.User
import ru.woodymsk.socialapp.presentation.common.compose.LoadAvatar
import ru.woodymsk.socialapp.presentation.common.compose.LoadImage

private const val BUNDLE_POST_KEY = "BUNDLE_POST_KEY"

@Composable
fun Profile(
    user: User,
    myPostList: List<Post>,
    onLogoutClick: () -> Unit,
    onLike: (postId: Int, likedByMe: Boolean) -> Unit,
    onEdit: (args: Bundle) -> Unit,
    onDelete: (id: String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileUserData(
            user = user,
            onLogoutClick = onLogoutClick,
        )
        PostListItems(
            myPostList = myPostList,
            onLike = onLike,
            onEdit = onEdit,
            onDelete = onDelete,
        )
    }
}

@Composable
private fun ProfileUserData(
    user: User,
    onLogoutClick: () -> Unit,
) {
    Column(
        Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(96.dp)
                        .padding(16.dp),
                ) {
                    LoadAvatar(url = user.avatar.orEmpty())
                }
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = user.name)
                        IconButton(onClick = { onLogoutClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = stringResource(R.string.exit_profile),
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.places_job))
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_job),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostListItems(
    myPostList: List<Post>,
    onLike: (id: Int, likedByMe: Boolean) -> Unit,
    onEdit: (args: Bundle) -> Unit,
    onDelete: (id: String) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
        ),
    ) {

        var openDeleteDialog by remember { mutableStateOf(false) }
        var postId by remember { mutableIntStateOf(0) }

        if (openDeleteDialog) {
            DeleteItemDialog(
                onClick = {
                    if (it) onDelete(postId.toString())
                    openDeleteDialog = false
                }
            )
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            items(items = myPostList, key = { item -> item.id }) { post ->
                val isContentExpanded = remember {
                    mutableStateOf(post.content.length <= 200)
                }
                val isContentVisibility = remember {
                    MutableTransitionState(false).apply {
                        targetState = !isContentExpanded.value
                    }
                }
                val isMenuExpanded = remember { mutableStateOf(false) }
                val likes = remember { mutableStateOf(post.likes) }
                val isLikedByMe = remember { mutableStateOf(post.likedByMe) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                ) {
                    Column(
                        Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(16.dp)
                            ) {
                                LoadAvatar(url = post.authorAvatar.orEmpty())
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column {
                                    Text(
                                        text = post.author,
                                        maxLines = 1,
                                    )
                                    Text(text = parseAndFormatDate(post.published))
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentSize(Alignment.TopEnd)
                                ) {
                                    IconButton(onClick = {
                                        isMenuExpanded.value = !isMenuExpanded.value
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = stringResource(R.string.more)
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
                                                val bundle = Bundle()
                                                bundle.putSerializable(BUNDLE_POST_KEY, post)
                                                onEdit(bundle)
                                                isMenuExpanded.value = false
                                            },
                                        )
                                        HorizontalDivider()
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.delete)) },
                                            onClick = {
                                                postId = post.id
                                                openDeleteDialog = true
                                                isMenuExpanded.value = false
                                            },
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider()
                        Text(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            text = post.content,
                            maxLines = if (isContentExpanded.value) Int.MAX_VALUE else 2,
                            textAlign = TextAlign.Justify,
                        )
                        Column {
                            AnimatedVisibility(visibleState = isContentVisibility) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp, bottom = 8.dp)
                                        .clickable {
                                            isContentExpanded.value = !isContentExpanded.value
                                            isContentVisibility.targetState =
                                                !isContentVisibility.targetState
                                        },
                                    text = stringResource(R.string.read_next),
                                )
                            }
                        }
                        Surface {
                            if (post.attachment?.type == AttachmentType.IMAGE) {
                                LoadImage(url = post.attachment.url)
                            }
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.padding(start = 16.dp),
                        ) {
                            IconButton(
                                onClick = { onLike }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        painterResource(id = if (isLikedByMe.value) {
                                            R.drawable.ic_like_filled_24dp
                                        } else {
                                            R.drawable.ic_like_outlined_24
                                        }),
                                        contentDescription = stringResource(R.string.context_menu_button),
                                        modifier = Modifier
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember {
                                                    MutableInteractionSource()
                                                }
                                            ) {
                                                onLike(post.id, post.likedByMe)
                                                isLikedByMe.value = !isLikedByMe.value
                                                likes.value = if (isLikedByMe.value) {
                                                    likes.value.inc()
                                                } else {
                                                    likes.value.dec()
                                                }
                                            }
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = if (likes.value != 0) {
                                            likes.value.toString()
                                        } else {
                                            stringResource(R.string.empty_text)
                                        },
                                        color = Color.Gray
                                    )
                                }
                            }
                            IconButton(
                                onClick = { }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = stringResource(id = R.string.context_menu_button),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}