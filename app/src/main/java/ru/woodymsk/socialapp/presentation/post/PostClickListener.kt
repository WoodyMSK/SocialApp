package ru.woodymsk.socialapp.presentation.post

interface PostClickListener {

    fun onLike(postId: Int, likedByMe: Boolean)

}