package ru.woodymsk.socialapp.presentation.event_details.compose

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.woodymsk.socialapp.presentation.event_details.EventDetailsViewModel
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsEvents
import ru.woodymsk.socialapp.presentation.navigation.model.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EventDetailsView(
    viewModel: EventDetailsViewModel,
    onNavigateTo: (Screen) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        EventDetailsScreen(
            state = uiState,
            onEvent = viewModel::onEvent,
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect(uiState) {
        when {
            uiState.error != null -> {
                uiState.error?.let {
                    Toast.makeText(
                        context,
                        it.code,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                viewModel.onEvent(EventDetailsEvents.Error(null))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { onNavigateTo(it) }
    }
}