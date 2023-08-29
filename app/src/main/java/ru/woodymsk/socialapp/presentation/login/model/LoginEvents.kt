package ru.woodymsk.socialapp.presentation.login.model

import androidx.annotation.StringRes
import ru.woodymsk.socialapp.error.AppError

sealed class LoginEvents {

    object LoginDataValid : LoginEvents()

    data class LoginDataError(@StringRes val loginError: Int) : LoginEvents()

    data class PasswordDataError(@StringRes val passwordError: Int) : LoginEvents()

    data class LoginSuccess(@StringRes val greeting: Int) : LoginEvents()

    data class LoginError(val appError: AppError) : LoginEvents()

}