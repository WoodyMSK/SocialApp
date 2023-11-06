package ru.woodymsk.socialapp.domain.login

import ru.woodymsk.socialapp.data.auth.model.Token

interface LoginRepository {

    suspend fun login(login: String, password: String): Token

}