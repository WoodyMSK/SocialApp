package ru.woodymsk.socialapp.domain.profile.interactor

import ru.woodymsk.socialapp.domain.profile.ProfileRepository
import ru.woodymsk.socialapp.domain.profile.model.User
import ru.woodymsk.socialapp.domain.post.mapper.PostMapper
import ru.woodymsk.socialapp.domain.post.model.Post
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val postMapper: PostMapper
) {

    suspend fun logout() = profileRepository.logout()

    suspend fun getMyProfileData(): User = profileRepository.getProfileData()

    suspend fun getMyPostList(): List<Post> =
        postMapper.mapPostsFromEntity(profileRepository.getProfilePostList())

    suspend fun likePost(postId: Int, likedByMe: Boolean) {
        if (likedByMe) {
            profileRepository.deleteLike(postId.toString())
        } else {
            profileRepository.like(postId.toString())
        }
    }

    suspend fun deletePost(id: String) = profileRepository.removePostById(id)
}