package ru.woodymsk.socialapp.data.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult.Error
import androidx.paging.RemoteMediator.MediatorResult.Success
import androidx.room.withTransaction
import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.post.db.PostDao
import ru.woodymsk.socialapp.data.post.db.PostDatabase
import ru.woodymsk.socialapp.data.post.db.PostKeyDao
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.data.post.model.PostKeyEntity
import ru.woodymsk.socialapp.domain.orZero
import ru.woodymsk.socialapp.domain.throwAppError

@ExperimentalPagingApi
class PostRemoteMediator(
    private val postService: PostService,
    private val postDao: PostDao,
    private val postKeyDao: PostKeyDao,
    private val postDatabase: PostDatabase,
    private val postMapper: PostMapper,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun initialize(): InitializeAction =
        if (postDao.isEmpty()) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {

        try {
            val pageSize = state.config.pageSize
            val response = when (loadType) {
                REFRESH -> {
                    postDao.removeAllPosts()
                    postService.getLatest(pageSize)
                }
                PREPEND -> return Success(true)
                APPEND -> {
                    val id = postKeyDao.minKey() ?: return Success(false)
                    postService.getBeforePost(id.toString(), pageSize)
                }
            }

            val postList = response.body().throwAppError(response)

            postDatabase.withTransaction {
                when (loadType) {
                    REFRESH -> {
                        postKeyDao.insertPostKeys(
                            listOf(
                                PostKeyEntity(
                                    type = PostKeyEntity.Type.PREPEND,
                                    id = postList.first().id.orZero(),
                                ),
                                PostKeyEntity(
                                    type = PostKeyEntity.Type.APPEND,
                                    id = postList.last().id.orZero(),
                                ),
                            )
                        )
                    }
                    PREPEND -> {
                        postKeyDao.insertPostKey(
                            PostKeyEntity(
                                type = PostKeyEntity.Type.PREPEND,
                                id = postList.first().id.orZero(),
                            )
                        )
                    }
                    APPEND -> {
                        postKeyDao.insertPostKey(
                            PostKeyEntity(
                                type = PostKeyEntity.Type.APPEND,
                                id = postList.last().id.orZero(),
                            )
                        )
                    }
                }

                postDao.insertPostList(postMapper.mapToEntity(postList))
            }

            return Success(postList.isEmpty())
        } catch (e: Exception) {
            return Error(e)
        }
    }
}