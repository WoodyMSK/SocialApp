package ru.woodymsk.socialapp.presentation.post.model

import ru.woodymsk.socialapp.error.AppError

sealed class NewPostEvents {
    object NewPostDataValid : NewPostEvents()
    object ContentDataError : NewPostEvents()
    object GoToPostListScreen : NewPostEvents()
    data class ErrorNewPosts(val appError: AppError) : NewPostEvents()
}