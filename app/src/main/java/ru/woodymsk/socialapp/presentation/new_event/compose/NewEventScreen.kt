package ru.woodymsk.socialapp.presentation.new_event.compose


import android.Manifest.permission.CAMERA
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType.IMAGE
import ru.woodymsk.socialapp.data.model.AttachmentType.VIDEO
import ru.woodymsk.socialapp.domain.copyUriToFile
import ru.woodymsk.socialapp.domain.createTempImageUri
import ru.woodymsk.socialapp.presentation.common.compose.LaunchSettingsDialog
import ru.woodymsk.socialapp.presentation.common.compose.LoadImage
import ru.woodymsk.socialapp.presentation.common.compose.PreviewVideoImageWithDurationAndPlayButton
import ru.woodymsk.socialapp.presentation.common.compose.RemoveAttachmentButton
import ru.woodymsk.socialapp.presentation.common.compose.rememberPermissionsState
import ru.woodymsk.socialapp.presentation.common.getImagePermissionType
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventUiState
import ru.woodymsk.socialapp.presentation.theme.typography


private const val URI_PACKAGE_SCHEME = "package"

// Ссылка на экран в Figma: https://www.figma.com/design/8z1sV6KIf6Sc1y02TrY2XS/Nmedia?node-id=30-8103&t=YwMhrRR7EaEeb7SZ-1
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    state: NewEventUiState = NewEventUiState(),
    onEvent: (NewEventEvents) -> Unit = {},
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState()
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(state.isShowDateTimeBottomSheet) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var isMediaPermission by remember { mutableStateOf(false) }
    // camera launcher
    val takePhotoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    onEvent(
                        NewEventEvents.AttachmentUpdated(
                            Attachment(
                                type = IMAGE,
                                url = uri.toString()
                            )
                        )
                    )
                }
            }
        }
    // media storage launcher
    val pickMediaLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    // defining the content type
                    val mimeType = context.contentResolver.getType(uri)
                    val attachmentType = when {
                        mimeType?.startsWith("image/") == true -> IMAGE
                        mimeType?.startsWith("video/") == true -> VIDEO
                        else -> null
                    }
                    val copiedFile = copyUriToFile(context, uri, mimeType)
                    copiedFile?.let { file ->
                        val fileUri = Uri.fromFile(file).toString()
                        onEvent(
                            NewEventEvents.AttachmentUpdated(
                                Attachment(
                                    type = attachmentType,
                                    url = fileUri
                                )
                            )
                        )
                    }
                }
            }
        )

    val getMediaPermission = rememberPermissionsState(
        permissions = listOf(getImagePermissionType()),
        onGrantedAction = {
            pickMediaLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageAndVideo)
            )
        },
        onPermanentlyDeniedAction = {
            showSettingsDialog = true
            isMediaPermission = true
        }
    )

    val getCameraPermission = rememberPermissionsState(
        permissions = listOf(CAMERA),
        onGrantedAction = {
            imageUri = createTempImageUri(context)
            imageUri?.let {
                takePhotoLauncher.launch(it)
            }
        },
        onPermanentlyDeniedAction = {
            showSettingsDialog = true
            isMediaPermission = false
        }
    )

    LaunchedEffect(state.isShowDateTimeBottomSheet) {
        showBottomSheet = state.isShowDateTimeBottomSheet
    }

    // focus on BasicTextField and launch keyboard at the start of the NewEventScreen
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        // top screen bar
        topBar = {
            TopAppBar(
                // screen name
                title = {
                    Text(
                        style = typography().titleLarge,
                        text = stringResource(id = R.string.new_event_screen_title)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceVariant),
                // back button
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_back_24),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        contentDescription = stringResource(id = R.string.back_button),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                keyboardController?.hide()
                                onEvent(NewEventEvents.GoToBackScreen)
                            }
                    )
                },
                // save event button
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_check_24),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        contentDescription = stringResource(id = R.string.save_button),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 20.dp)
                            .clickable(
                                enabled = !state.isLoading,
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                onEvent(NewEventEvents.CreateEvent)
                            }
                    )
                },
            )
        },
        // FAB
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(NewEventEvents.DateTimeBottomSheetState(true))
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = stringResource(R.string.event_date_selection_button),
                )
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
        // bottom screen bar
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = Color.Black,
            ) {
                // add a photo from the camera
                Image(
                    painter = painterResource(id = R.drawable.ic_photo_camera_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = stringResource(id = R.string.photo_camera_button),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            getCameraPermission.launchPermissionRequestsAndAction()
                        }
                )
                // add a photo from the gallery
                Image(
                    painter = painterResource(id = R.drawable.ic_attach_file_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = stringResource(id = R.string.attach_file_button),
                    modifier = Modifier
                        .padding(start = 12.dp, end = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            getMediaPermission.launchPermissionRequestsAndAction()
                        }
                )
                // dialog with the list of users button
                Image(
                    painter = painterResource(id = R.drawable.ic_people_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = stringResource(id = R.string.speaker_selection_button),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            // TODO add dialog with the list of users function
                        }
                )
                // add to choosing a location button
                Image(
                    painter = painterResource(id = R.drawable.ic_location_pin_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = stringResource(id = R.string.choosing_location_button),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            // TODO add to choosing a location function
                        }
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .wrapContentHeight(Alignment.Top)
                    .verticalScroll(rememberScrollState()),
            ) {
                // event text field
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = state.event.content,
                    onValueChange = {
                        onEvent(NewEventEvents.ContentUpdated(it))
                    },
                    textStyle = typography().bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    maxLines = 30,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    decorationBox = { innerTextField ->
                        innerTextField()
                        if (state.event.content.isEmpty()) {
                            Text(
                                text = stringResource(R.string.event_content_placeholder),
                                style = typography().bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                )

                if (state.event.attachment?.type == IMAGE) {
                    // event image with remove button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        LoadImage(url = state.event.attachment.url)
                        // remove button
                        RemoveAttachmentButton(
                            onRemove = {
                                onEvent(NewEventEvents.AttachmentUpdated(null))
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        )
                    }
                }

                if (state.event.attachment?.type == VIDEO) {
                    // video preview with remove button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        PreviewVideoImageWithDurationAndPlayButton(
                            videoUri = state.event.attachment.url,
                            modifier = Modifier.fillMaxWidth()
                        )
                        // remove button
                        RemoveAttachmentButton(
                            onRemove = {
                                onEvent(NewEventEvents.AttachmentUpdated(null))
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        )
                    }
                }
            }
            // bottomSheet
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        onEvent(NewEventEvents.DateTimeBottomSheetState(false))
                    },
                    sheetState = sheetState
                ) {
                    // bottomSheet content
                    NewEventDateTimeBottomSheet(
                        state = state.event,
                        onEvent = onEvent,
                    )
                }
            }
        }
    )

    LaunchSettingsDialog(
        showDialog = showSettingsDialog,
        title = if (isMediaPermission) stringResource(R.string.media_access_is_required)
        else stringResource(R.string.сamera_access_is_required),
        onDismiss = { showSettingsDialog = false },
        onConfirm = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts(URI_PACKAGE_SCHEME, context.packageName, null)
            }
            context.startActivity(intent)
            showSettingsDialog = false
        },
        onCancel = { showSettingsDialog = false }
    )
}

@Composable
@Preview
fun NewEventScreenPreview() {
    NewEventScreen()
}