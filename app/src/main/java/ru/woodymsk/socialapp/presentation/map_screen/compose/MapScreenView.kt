package ru.woodymsk.socialapp.presentation.map_screen.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.presentation.map_screen.MapScreenViewModel
import ru.woodymsk.socialapp.presentation.map_screen.model.MapEvents
import ru.woodymsk.socialapp.domain.map_screen.model.ObjectDetails
import ru.woodymsk.socialapp.presentation.navigation.model.Screen

@Composable
fun MapScreenView(
    viewModel: MapScreenViewModel,
    onNavigateTo: (Screen) -> Unit,
    onBackWithResult: (Coords) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Box(Modifier.fillMaxSize()) {
        MapScreen(
            state = uiState,
            onEvent = viewModel::onEvent
        )

        when {
            uiState.showDetailsDialog && uiState.selectedObject != null -> {
                DetailsDialog(
                    objectDetails = uiState.selectedObjectDetails as ObjectDetails,
                    onEvent = viewModel::onEvent,
                    onConfirm = {
                        uiState.selectedObject
                            ?.geometry
                            ?.firstOrNull()
                            ?.point
                            ?.let { point ->
                                onBackWithResult(
                                    Coords(
                                        lat = point.latitude,
                                        long = point.longitude
                                    )
                                )
                            }
                    },
                    onDismiss = { viewModel.onEvent(MapEvents.DismissDetailsDialog) },
                )
            }

            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { onNavigateTo(it) }
    }

    LaunchedEffect(uiState.showToast) {
        uiState.showToast?.let { text ->
            clipboardManager.setText(AnnotatedString(text))
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(MapEvents.HideToast)
        }
    }
}