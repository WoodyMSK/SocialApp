package ru.woodymsk.socialapp.data.registration

import com.google.gson.Gson
import ru.woodymsk.socialapp.data.api.AuthService
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.data.model.ErrorResponse
import ru.woodymsk.socialapp.domain.registration.RegistrationRepository
import ru.woodymsk.socialapp.error.AppError.ApiError
import withContextIO
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val gson: Gson,
) : RegistrationRepository {

    @Inject
    lateinit var auth: AppAuth

    override suspend fun registration(login: String, password: String, name: String): Token =
        withContextIO {
            val response = authService.registerUser(login, password, name)
            val body = response.body()
                ?: throw ApiError(
                    gson.fromJson(
                        response.errorBody()?.string(), ErrorResponse::class.java
                    )
                    .reason
                )
            auth.setAuth(body.id, body.token.orEmpty())

            return@withContextIO body
        }
}