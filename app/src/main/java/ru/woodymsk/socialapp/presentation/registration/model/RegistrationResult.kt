package ru.woodymsk.socialapp.presentation.registration.model

import androidx.annotation.StringRes

sealed class RegistrationResult {

    data class Success(val userName: String) : RegistrationResult()

    data class Error(@StringRes val error: Int) : RegistrationResult()
}