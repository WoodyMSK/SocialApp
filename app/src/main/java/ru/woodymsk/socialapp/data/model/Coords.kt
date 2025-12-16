package ru.woodymsk.socialapp.data.model

import kotlinx.serialization.Serializable
import java.io.Serializable as javaSerializable

@Serializable
data class Coords(
    val lat: Double,
    val long: Double,
) : javaSerializable {
    override fun toString(): String {
        return "$lat, $long"
    }
}