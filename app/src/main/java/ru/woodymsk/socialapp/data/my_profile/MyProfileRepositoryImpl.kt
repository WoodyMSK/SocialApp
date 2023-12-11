package ru.woodymsk.socialapp.data.my_profile

import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.my_profile.MyProfileRepository
import ru.woodymsk.socialapp.domain.post.PostRepository
import javax.inject.Inject

class MyProfileRepositoryImpl @Inject constructor(
    private val postRepository: PostRepository,
) : MyProfileRepository {

    @Inject
    lateinit var auth: AppAuth

    override suspend fun logout() {
        auth.removeAuth()
        postRepository.removeAllDbPosts()
    }
}