package ru.woodymsk.socialapp.domain.auth

import ru.woodymsk.socialapp.data.auth.model.Token

interface RegistrationRepository {

    suspend fun registration(login: String, password: String, name: String): Token

}