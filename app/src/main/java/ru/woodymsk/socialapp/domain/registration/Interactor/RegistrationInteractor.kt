package ru.woodymsk.socialapp.domain.registration.Interactor

import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.domain.registration.RegistrationRepository
import javax.inject.Inject

class RegistrationInteractor @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {

    suspend fun registration(login: String, password: String, name: String): Token =
        registrationRepository.registration(login, password, name)

}