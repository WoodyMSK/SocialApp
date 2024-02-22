package ru.woodymsk.socialapp.presentation.post.adapter

import ru.woodymsk.socialapp.domain.post.model.Post

interface PostClickListener {

    fun onLike(postId: Int, likedByMe: Boolean)
    fun onEdit(post: Post)
    fun onDelete(id: Int)

}