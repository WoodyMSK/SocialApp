package ru.woodymsk.socialapp.domain.login.Interactor

import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.domain.login.LoginRepository
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val loginRepository: LoginRepository,
) {

    fun logout() = loginRepository.logout()

    suspend fun login(login: String, password: String): Token =
        loginRepository.login(login, password)

}