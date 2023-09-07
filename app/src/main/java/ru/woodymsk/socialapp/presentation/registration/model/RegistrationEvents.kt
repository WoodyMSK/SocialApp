package ru.woodymsk.socialapp.presentation.registration.model

import androidx.annotation.StringRes
import ru.woodymsk.socialapp.error.AppError

sealed class RegistrationEvents {

    object RegistrationDataValid : RegistrationEvents()

    data class LoginDataError(@StringRes val loginError: Int) : RegistrationEvents()

    data class PasswordDataError(@StringRes val passwordError: Int) : RegistrationEvents()

    data class ConfirmPasswordError(@StringRes val confirmPasswordError: Int) : RegistrationEvents()

    data class NameDataError(@StringRes val nameError: Int) : RegistrationEvents()

    data class RegistrationSuccess(val userName: String) : RegistrationEvents()

    data class RegistrationError(val appError: AppError) : RegistrationEvents()

}