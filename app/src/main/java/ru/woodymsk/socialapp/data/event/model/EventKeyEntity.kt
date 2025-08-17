package ru.woodymsk.socialapp.data.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventKeyEntity(
    @PrimaryKey
    val type: Type,
    val id: Int,
) {

    enum class Type {
        PREPEND,
        APPEND,
    }
}