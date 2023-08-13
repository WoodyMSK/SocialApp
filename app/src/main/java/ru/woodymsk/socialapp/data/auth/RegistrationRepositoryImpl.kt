package ru.woodymsk.socialapp.data.auth

import ru.woodymsk.socialapp.data.api.AuthService
import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.domain.auth.RegistrationRepository
import ru.woodymsk.socialapp.error.AppError.ApiError
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : RegistrationRepository {

    @Inject
    lateinit var auth: AppAuth

    override suspend fun registration(login: String, password: String, name: String): Token =
        withContextIO(
            handler
        ) {
            val response = authService.registerUser(login, password, name)
            val body = response.body() ?: throw ApiError(response.message())
            auth.setAuth(body.id, body.token.orEmpty())

            return@withContextIO body
        }
}