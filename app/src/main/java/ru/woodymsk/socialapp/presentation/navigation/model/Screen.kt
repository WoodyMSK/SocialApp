package ru.woodymsk.socialapp.presentation.navigation.model

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object EventScreen: Screen()

    @Serializable
    data object NewEventScreen: Screen()

    @Serializable
    data object PopBackStack: Screen()
}