package ru.woodymsk.socialapp.presentation.registration.model

import androidx.annotation.StringRes

sealed class RegistrationFormState {

    object RegistrationDataValid: RegistrationFormState()

    data class LoginError(@StringRes val loginError: Int): RegistrationFormState()

    data class PasswordError(@StringRes val passwordError: Int): RegistrationFormState()

    data class ConfirmPasswordError(@StringRes val confirmPasswordError: Int): RegistrationFormState()

    data class NameError(@StringRes val nameError: Int): RegistrationFormState()

}