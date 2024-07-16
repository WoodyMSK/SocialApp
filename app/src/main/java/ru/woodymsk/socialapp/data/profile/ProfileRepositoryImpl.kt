package ru.woodymsk.socialapp.data.profile

import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.api.ProfileService
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.data.post.db.PostDao
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.domain.profile.ProfileRepository
import ru.woodymsk.socialapp.domain.profile.model.User
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.domain.throwAppError
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val profileService: ProfileService,
    private val postService: PostService,
    private val postMapper: PostMapper,
    private val postDao: PostDao,
    private val auth: AppAuth,
) : ProfileRepository {

    override suspend fun logout() {
        auth.removeAuth()
        postRepository.removeAllDbPosts()
    }

    override suspend fun getProfileData(): User =
        withContextIO(handler) {
            val response = profileService.getProfileData(auth.authStateFlow.value.id.toString())
            response.body().throwAppError(response)
        }

    override suspend fun getProfilePostList(): List<PostEntity> =
        withContextIO(handler) {
            val response = profileService.getProfilePostList()
            postMapper.mapToEntity(response.body().orEmpty())
        }

    override suspend fun like(id: String): PostEntity =
        withContextIO(handler) {
            postDao.like(id.toInt())
            val response = postService.like(id)
            postMapper.mapSinglePostToEntity(response.body().throwAppError(response))
        }

    override suspend fun deleteLike(id: String): PostEntity =
        withContextIO(handler) {
            postDao.deleteLike(id.toInt())
            val response = postService.deleteLike(id)
            postMapper.mapSinglePostToEntity(response.body().throwAppError(response))
        }

    override suspend fun removePostById(id: String) =
        withContextIO(handler) {
            val response = postService.removePostById(id)
            if (!response.isSuccessful) {
                response.body().throwAppError(response)
            }
            postDao.removePostById(id.toInt())
        }
}