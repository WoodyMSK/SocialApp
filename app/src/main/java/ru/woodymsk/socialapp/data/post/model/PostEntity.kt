package ru.woodymsk.socialapp.data.post.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.woodymsk.socialapp.data.model.Attachment

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    @Embedded
    val attachment: Attachment?,
    val likes: Int,
)