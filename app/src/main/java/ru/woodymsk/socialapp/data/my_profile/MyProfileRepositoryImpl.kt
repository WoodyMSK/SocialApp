package ru.woodymsk.socialapp.data.my_profile

import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.my_profile.MyProfileRepository
import javax.inject.Inject

class MyProfileRepositoryImpl @Inject constructor() : MyProfileRepository {

    @Inject
    lateinit var auth: AppAuth

    override fun logout() = auth.removeAuth()
}