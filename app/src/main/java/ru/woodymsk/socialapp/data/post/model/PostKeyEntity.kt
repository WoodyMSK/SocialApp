package ru.woodymsk.socialapp.data.post.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostKeyEntity(
    @PrimaryKey
    val type: Type,
    val id: Int,
) {

    enum class Type {
        PREPEND,
        APPEND,
    }
}