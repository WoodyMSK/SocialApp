package ru.woodymsk.socialapp.presentation.registration.model

sealed class RegistrationResult {

    data class Success(val userName: String) : RegistrationResult()

    data class Error(val error: Int) : RegistrationResult()
}