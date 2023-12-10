package ru.woodymsk.socialapp.domain.my_profile.interactor

import ru.woodymsk.socialapp.domain.my_profile.MyProfileRepository
import javax.inject.Inject

class MyProfileInteractor @Inject constructor(
    private val myProfileRepository: MyProfileRepository,
) {

    suspend fun logout() = myProfileRepository.logout()
}