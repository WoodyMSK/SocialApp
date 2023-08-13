package ru.woodymsk.socialapp.domain.auth

import ru.woodymsk.socialapp.data.auth.model.Token

interface LoginRepository {

    fun logout()
    suspend fun login(login: String, password: String): Token

}