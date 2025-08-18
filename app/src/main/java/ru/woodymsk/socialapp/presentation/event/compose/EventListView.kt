package ru.woodymsk.socialapp.presentation.event.compose

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.navigation.model.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EventListView(
    viewModel: EventListViewModel,
    onNavigateTo: (Screen) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        val view = LocalView.current
        val window = (view.context as Activity).window
        window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()

        EventListScreen(
            state = uiState,
            onEvent = viewModel::onEvent,
        )
    }

    LaunchedEffect(Unit) {
        Log.d("EventListView","LaunchedEffect(Unit), GoToNewEventScreen")
        viewModel.navigationEvents.collect { onNavigateTo(it) }
    }
}