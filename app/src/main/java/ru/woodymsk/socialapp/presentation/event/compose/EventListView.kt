package ru.woodymsk.socialapp.presentation.event.compose

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.event.model.EventUiState.LoadingState
import ru.woodymsk.socialapp.presentation.event.model.EventUiState.ShowEvents
import ru.woodymsk.socialapp.presentation.navigation.model.Screen

@Composable
fun EventListView(
    viewModel: EventListViewModel,
    onNavigateTo: (Screen) -> Unit,
) {

    val uiState by viewModel.state.collectAsState(LoadingState)
    val isAuth by viewModel.isAuth.collectAsState()

    Box(Modifier.fillMaxSize()) {

        val view = LocalView.current
        val window = (view.context as Activity).window
        window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()

        when (uiState) {
            is ShowEvents -> {
                EventListScreen(
                    eventList = (uiState as ShowEvents).events,
                    onNavigateTo = onNavigateTo,
                    onEvent = viewModel::onEvent,
                    isAuth = isAuth,
                )
            }

            is LoadingState -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.inversePrimary,
                )
            }
        }
    }
}