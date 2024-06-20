package ru.woodymsk.socialapp.domain.profile.model

data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?,
)