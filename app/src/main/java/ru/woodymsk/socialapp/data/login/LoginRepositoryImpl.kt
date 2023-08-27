package ru.woodymsk.socialapp.data.login

import retrofit2.HttpException
import ru.woodymsk.socialapp.data.api.AuthService
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.domain.login.LoginRepository
import ru.woodymsk.socialapp.error.AppError
import withContextIO
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : LoginRepository {

    @Inject
    lateinit var auth: AppAuth

    override fun logout() = auth.removeAuth()

    override suspend fun login(login: String, password: String): Token = withContextIO {
        val response = authService.authUser(login, password)
        val body = response.body() ?: throw AppError.handleError(HttpException(response))
        auth.setAuth(body.id, body.token.orEmpty())

        return@withContextIO body
    }
}