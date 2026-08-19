package ru.woodymsk.socialapp.presentation.common.compose

data class MediaPlaybackState(
    val playingEventId: Int? = null,
    val isPlaying: Boolean = false,
)