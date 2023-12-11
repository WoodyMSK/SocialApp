package ru.woodymsk.socialapp.data.login

import com.google.gson.Gson
import ru.woodymsk.socialapp.data.api.AuthService
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.data.model.ErrorResponse
import ru.woodymsk.socialapp.domain.login.LoginRepository
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.error.AppError.ApiError
import withContextIO
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val gson: Gson,
    private val postRepository: PostRepository,
) : LoginRepository {

    @Inject
    lateinit var auth: AppAuth

    override suspend fun login(login: String, password: String): Token = withContextIO {
        val response = authService.authUser(login, password)
        val body = response.body()
            ?: throw ApiError(
                gson.fromJson(
                    response.errorBody()?.string(), ErrorResponse::class.java
                )
                .reason
            )
        auth.setAuth(body.id, body.token.orEmpty())
        postRepository.removeAllDbPosts()

        return@withContextIO body
    }
}