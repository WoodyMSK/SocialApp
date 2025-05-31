package ru.woodymsk.socialapp.presentation.event.compose

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.event.model.EventEvents
import ru.woodymsk.socialapp.presentation.navigation.model.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EventListView(
    viewModel: EventListViewModel,
    onNavigateTo: (Screen) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        val view = LocalView.current
        val window = (view.context as Activity).window
        window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()

        EventListScreen(
            state = viewModel.uiState.value,
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
                viewModel.onEvent(EventEvents.Error(null))
                viewModel.onEvent(EventEvents.Loading(false))
            }

            uiState.isGoToNewEventScreen -> {
                viewModel.onEvent(EventEvents.GoToNewEventScreen(false))
                onNavigateTo(Screen.NewEventScreen)
            }
        }
    }
}