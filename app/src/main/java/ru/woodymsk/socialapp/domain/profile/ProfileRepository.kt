package ru.woodymsk.socialapp.domain.profile

import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.domain.profile.model.User

interface ProfileRepository {

    suspend fun logout()
    suspend fun getProfileData(): User
    suspend fun getProfilePostList(): List<PostEntity>
    suspend fun like(id: String): PostEntity
    suspend fun deleteLike(id: String): PostEntity
    suspend fun removePostById(id: String)
}