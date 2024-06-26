package ru.woodymsk.socialapp.presentation.profile.model

import ru.woodymsk.socialapp.domain.profile.model.User
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.error.AppError

sealed class ProfileEvents {
    data class ShowProfile(val user: User, val posts: List<Post>) : ProfileEvents()
    data class ProfileError(val appError: AppError) : ProfileEvents()
    object LoadingState : ProfileEvents()
}