package ru.woodymsk.socialapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Coords(
    val lat: Double,
    val long: Double,
)