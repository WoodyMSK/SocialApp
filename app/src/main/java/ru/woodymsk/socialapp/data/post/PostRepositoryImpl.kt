package ru.woodymsk.socialapp.data.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.Media
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.data.post.db.PostDao
import ru.woodymsk.socialapp.data.post.db.PostDatabase
import ru.woodymsk.socialapp.data.post.db.PostKeyDao
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.domain.throwAppError
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val postMapper: PostMapper,
    private val postDao: PostDao,
    private val postKeyDao: PostKeyDao,
    private val postDatabase: PostDatabase,
) : PostRepository {

    companion object {
        const val PAGE_SIZE = 10
        const val FILE_NAME = "file"
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagedPostList(): Flow<PagingData<PostEntity>> =
        withContextIO {
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = true,
                    prefetchDistance = 3 * PAGE_SIZE,
                    initialLoadSize = 2 * PAGE_SIZE,
                ),
                pagingSourceFactory = postDao::getPagingSource,
                remoteMediator = PostRemoteMediator(
                    postService = postService,
                    postDao = postDao,
                    postKeyDao = postKeyDao,
                    postDatabase = postDatabase,
                    postMapper = postMapper,
                )
            )
        }
            .flow

    override suspend fun getAllPostList(): List<PostEntity> =
        withContextIO(handler) {
            val response = postService.getAllPostList()
            postMapper.mapToDao(response.body().orEmpty())
        }

    override suspend fun getPostById(id: String): PostEntity =
        withContextIO(handler) {
            val response = postService.getPostById(id)
            postMapper.mapSinglePostToEntity(
                response.body().throwAppError(response)
            )
        }

    override suspend fun like(id: String): PostEntity =
        withContextIO(handler) {
            postDao.like(id.toInt())
            val response = postService.like(id)
            postMapper.mapSinglePostToEntity(
                response.body().throwAppError(response)
            )
        }

    override suspend fun deleteLike(id: String): PostEntity =
        withContextIO(handler) {
            postDao.deleteLike(id.toInt())
            val response = postService.deleteLike(id)
            postMapper.mapSinglePostToEntity(
                response.body().throwAppError(response)
            )
        }

    override suspend fun createPost(postEntity: PostEntity) =
        withContextIO(handler) {
            val response = postService.createPost(postMapper.mapSinglePostToCreate(postEntity))
            val postResponse = postMapper.mapSinglePostToEntity(
                response.body().throwAppError(response)
            )
            postDao.insertPost(postResponse)
        }

    override suspend fun createPostWithAttachment(postEntity: PostEntity, upload: MediaUpload) =
        withContextIO(handler) {
            val media = uploadMedia(upload)
            val postWithAttachment =
                postEntity.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
            createPost(postWithAttachment)
        }

    override suspend fun uploadMedia(upload: MediaUpload): Media =
        withContextIO(handler) {
            val media = MultipartBody.Part.createFormData(
                name = FILE_NAME,
                filename = upload.file.name,
                body = upload.file.asRequestBody(),
            )
            val response = postService.uploadMedia(media)
            response.body().throwAppError(response)
        }
}