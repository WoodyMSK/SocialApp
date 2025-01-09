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
import ru.woodymsk.socialapp.presentation.event.EventViewModel
import ru.woodymsk.socialapp.presentation.event.model.EventsEvent
import ru.woodymsk.socialapp.presentation.event.model.EventsEvent.LoadingState
import ru.woodymsk.socialapp.presentation.event.model.EventsEvent.ShowEvents

@Composable
fun EventView(eventViewModel: EventViewModel) {
    val state: EventsEvent by eventViewModel.events.collectAsState(LoadingState)

    Box(Modifier.fillMaxSize()) {

        val view = LocalView.current
        val window = (view.context as Activity).window
        window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()

        when (val event = state) {
            is ShowEvents -> {
                EventScreen(
                    eventList = event.events,
                    isAuth = eventViewModel::isAuth,
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