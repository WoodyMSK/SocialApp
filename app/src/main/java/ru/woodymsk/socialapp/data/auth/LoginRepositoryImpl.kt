package ru.woodymsk.socialapp.data.auth

import ru.woodymsk.socialapp.data.api.AuthService
import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.domain.auth.LoginRepository
import ru.woodymsk.socialapp.error.AppError.ApiError
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : LoginRepository {

    @Inject
    lateinit var auth: AppAuth

    override fun logout() {
        auth.removeAuth()
    }

    override suspend fun login(login: String, password: String): Token = withContextIO(handler) {
        val response = authService.authUser(login, password)
        val body = response.body() ?: throw ApiError(response.message())
        auth.setAuth(body.id, body.token.orEmpty())

        return@withContextIO body
    }
}