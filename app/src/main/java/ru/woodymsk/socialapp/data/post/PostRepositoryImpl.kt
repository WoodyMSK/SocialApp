package ru.woodymsk.socialapp.data.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.api.PostService
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
            postMapper.mapSinglePostToDao(
                response.body().throwAppError(response)
            )
        }

    override suspend fun like(id: String): PostEntity =
        withContextIO(handler) {
            postDao.like(id.toInt())
            val response = postService.like(id)
            postMapper.mapSinglePostToDao(
                response.body().throwAppError(response)
            )
        }

    override suspend fun deleteLike(id: String): PostEntity =
        withContextIO(handler) {
            postDao.deleteLike(id.toInt())
            val response = postService.deleteLike(id)
            postMapper.mapSinglePostToDao(
                response.body().throwAppError(response)
            )
        }
}