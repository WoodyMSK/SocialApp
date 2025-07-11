package ru.woodymsk.socialapp.presentation.navigation.model

import kotlinx.serialization.Serializable
import ru.woodymsk.socialapp.domain.event.model.Event

sealed class Screen {

    @Serializable
    data object EventListScreen: Screen()

    @Serializable
    data class NewEventScreen(val event: Event?): Screen()

    @Serializable
    data object PopBackStack: Screen()
}