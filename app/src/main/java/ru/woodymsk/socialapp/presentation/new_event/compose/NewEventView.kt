package ru.woodymsk.socialapp.presentation.new_event.compose

import androidx.compose.runtime.Composable
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import ru.woodymsk.socialapp.presentation.new_event.NewEventViewModel

@Composable
fun NewEventView(
    viewModel: NewEventViewModel,
    onNavigateTo: (Screen) -> Unit,
) {
    NewEventScreen(
        state = viewModel.state,
        onNavigateTo = onNavigateTo,
        onEvent = viewModel::onEvent,
    )
}