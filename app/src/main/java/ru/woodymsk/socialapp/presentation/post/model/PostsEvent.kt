package ru.woodymsk.socialapp.presentation.post.model

import androidx.annotation.StringRes
import androidx.paging.PagingData
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.error.AppError

sealed class PostsEvent {
    data class ShowPosts(val events: PagingData<Post>) : PostsEvent()
    data class ErrorPosts(val appError: AppError) : PostsEvent()
    data class ErrorAuth(@StringRes val registrationRequire: Int) : PostsEvent()
}